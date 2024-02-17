package com.dragonguard.backend.global.audit;

import org.hibernate.annotations.Where;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 김승진
 * @description 삭제 시간의 저장을 통해 soft delete를 구현할 엔티티들에 붙을 annotation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Where(clause = "deleted_at is null")
public @interface SoftDelete {}
