package com.dragonguard.backend.global;

import org.hibernate.annotations.Where;

/**
 * @author 김승진
 * @description 삭제 시간의 저장을 통해 soft delete를 구현할 엔티티들에 붙을 annotation
 */

@Where(clause = "deleted_at is null")
public @interface SoftDelete {}
