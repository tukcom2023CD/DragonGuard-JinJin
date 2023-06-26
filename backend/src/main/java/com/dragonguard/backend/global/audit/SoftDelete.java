package com.dragonguard.backend.global.audit;

import org.hibernate.annotations.Where;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 김승진
 * @description 삭제 시간의 저장을 통해 soft delete를 구현할 엔티티들에 붙을 annotation
 */

@Inherited
@Target(TYPE)
@Retention(RUNTIME)
@Where(clause = "deleted_at is null")
public @interface SoftDelete {}
