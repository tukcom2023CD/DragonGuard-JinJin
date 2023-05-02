package com.dragonguard.backend.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogIntroduction {
    @Pointcut("execution(* com.dragonguard.backend..*Controller*.*(..))")
    public void allController() {
    }

    @Pointcut("execution(* com.dragonguard.backend..*Service*.*(..))")
    public void allService() {
    }

    @Pointcut("execution(* com.dragonguard.backend..*Repository*.*(..))")
    public void allRepository() {
    }

    @Before("allController()")
    public void controllerLog(JoinPoint joinPoint) {
        log.info(
                "METHOD : {}, ARGS : {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @Before("allService() || allRepository()")
    public void serviceAndRepositoryLog(JoinPoint joinPoint) {
        log.debug(
                "METHOD : {}, ARGS : {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }
}
