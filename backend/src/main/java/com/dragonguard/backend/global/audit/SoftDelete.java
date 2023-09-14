package com.dragonguard.backend.global.audit;

import org.hibernate.annotations.Where;

import javax.persistence.EntityListeners;
import java.lang.annotation.*;

/**
 * @author 김승진
 * @description 삭제 시간의 저장을 통해 soft delete를 구현할 엔티티들에 붙을 annotation
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
public @interface SoftDelete {}
