package com.dragonguard.backend.aspect;

import com.dragonguard.backend.config.cronjob.CronjobProperties;
import com.dragonguard.backend.global.exception.CronjobException;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 김승진
 * @description cronjob 인증을 위한 aop
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CronjobAuthAspect {

    private final CronjobProperties cronjobProperties;

    @Around("execution(* com.dragonguard.backend.cronjob.CronJobController*.*(..))")
    public Object adminLoginCheck(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final HttpServletRequest req =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        final String header = req.getHeader(cronjobProperties.getHeader());

        if (!header.equals(cronjobProperties.getAuth())) {
            throw new CronjobException();
        }

        return proceedingJoinPoint.proceed();
    }
}
