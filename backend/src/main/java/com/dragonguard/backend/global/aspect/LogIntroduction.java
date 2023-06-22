package com.dragonguard.backend.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 각종 클래스의 요청에 대해 AOP로 로깅을 진행하는 클래스
 */

@Slf4j
@Aspect
@Component
public class LogIntroduction {
    private static final String FORMAT = "METHOD : {}, ARGS : {}";

    @Pointcut("execution(* com.dragonguard.backend..*Controller*.*(..))")
    public void allController() {}

    @Pointcut("execution(* com.dragonguard.backend..*Service*.*(..))")
    public void allService() {}

    @Pointcut("execution(* com.dragonguard.backend..*Repository*.*(..))")
    public void allRepository() {}

    @Pointcut("execution(* com.dragonguard.backend..*Producer*.*(..))")
    public void allConsumer() {}

    @Pointcut("execution(* com.dragonguard.backend..*Producer*.*(..))")
    public void allProducer() {}

    @Pointcut("execution(* com.dragonguard.backend..*Client*.*(..))")
    public void allClient() {}

    @Before("allController()")
    public void controllerLog(JoinPoint joinPoint) {
        infoLogging(joinPoint);
    }

    @Before("allService() || allRepository()")
    public void serviceAndRepositoryLog(JoinPoint joinPoint) {
        infoLogging(joinPoint);
    }

    @Before("allClient()")
    public void clientLog(JoinPoint joinPoint) {
        debugLogging(joinPoint);
    }

    @Before("allConsumer() || allProducer()")
    public void consumerAndLog(JoinPoint joinPoint) {
        debugLogging(joinPoint);
    }

    private void infoLogging(JoinPoint joinPoint) {
        log.info(
                FORMAT,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    private void debugLogging(JoinPoint joinPoint) {
        log.debug(
                FORMAT,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }
}
