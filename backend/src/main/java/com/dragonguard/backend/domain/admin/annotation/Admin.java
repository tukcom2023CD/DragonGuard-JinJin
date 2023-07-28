package com.dragonguard.backend.domain.admin.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

/**
 * @author 김승진
 * @description 관리자 권한 확인 Annotation
 */

@PreAuthorize("hasRole('ADMIN')")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Admin {}
