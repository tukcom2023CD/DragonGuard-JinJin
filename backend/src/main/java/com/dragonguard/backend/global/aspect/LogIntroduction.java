package com.dragonguard.backend.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    @Around("allController()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) {
        log.info(
                "METHOD : {}, ARGS : {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            log.info("METHOD : {}, RESULT : {}", joinPoint.getSignature().toShortString(), result);
            return result;
        } catch (Throwable e) {
            return null;
        }
    }

    @Around("allService() || allRepository()")
    public Object serviceAndRepositoryLog(ProceedingJoinPoint joinPoint) { // TODO 배포시 log.debug() 로 수정
        log.info(
                "METHOD : {}, ARGS : {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            log.info("METHOD : {}, RESULT : {}", joinPoint.getSignature().toShortString(), result);
            return result;
        } catch (Throwable e) {
            return null;
        }
    }
}
