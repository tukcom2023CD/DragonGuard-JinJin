package com.dragonguard.backend.aspect;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

/**
 * @author 김승진
 * @description 각종 클래스의 요청에 대해 AOP로 로깅을 진행하는 클래스
 */
@Slf4j
@Aspect
@Component
public class LogIntroduction {
    private static final String LOG_FORMAT = "METHOD : {}";

    @Pointcut("execution(* com.dragonguard.backend..*Controller*.*(..))")
    public void allController() {}

    @Pointcut("execution(* com.dragonguard.backend..*Service*.*(..))")
    public void allService() {}

    @Pointcut("execution(* com.dragonguard.backend..*Repository*.*(..))")
    public void allRepository() {}

    @Pointcut("execution(* com.dragonguard.backend..*Consumer*.*(..))")
    public void allConsumer() {}

    @Pointcut("execution(* com.dragonguard.backend..*Producer*.*(..))")
    public void allProducer() {}

    @Pointcut("execution(* com.dragonguard.backend..*Client*.*(..))")
    public void allClient() {}

    @Before("allController()")
    public void controllerLog(final JoinPoint joinPoint) {
        logging(joinPoint, log::info);
    }

    @Before("allService() || allRepository()")
    public void serviceAndRepositoryLog(final JoinPoint joinPoint) {
        logging(joinPoint, log::debug);
    }

    @Before("allClient()")
    public void clientLog(final JoinPoint joinPoint) {
        logging(joinPoint, log::debug);
    }

    @Before("allConsumer() || allProducer()")
    public void consumerAndLog(final JoinPoint joinPoint) {
        logging(joinPoint, log::debug);
    }

    private void logging(final JoinPoint joinPoint, final BiConsumer<String, String> consumer) {
        consumer.accept(LOG_FORMAT, joinPoint.getSignature().toShortString());
    }
}
