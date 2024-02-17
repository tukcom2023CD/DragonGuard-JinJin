package com.dragonguard.backend.aspect;

import com.dragonguard.backend.global.annotation.DistributedLock;
import com.dragonguard.backend.global.exception.DistributedLockUnavailableException;
import com.dragonguard.backend.utils.CustomSpringELParser;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 김승진
 * @description 레디스 분산락을 위한 aop
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final TransactionAspect transactionAspect;
    private final CustomSpringELParser customSpringELParser;

    @Around("@annotation(com.dragonguard.backend.global.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key =
                REDISSON_LOCK_PREFIX
                        + customSpringELParser.getDynamicValue(
                                signature.getParameterNames(),
                                joinPoint.getArgs(),
                                distributedLock.name());
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available =
                    rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit());
            if (!available) {
                return false;
            }
            return transactionAspect.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
            } catch (DistributedLockUnavailableException ignored) {
            }
        }
    }
}
