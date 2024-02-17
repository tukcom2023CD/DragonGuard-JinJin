package com.dragonguard.backend.domain.organization.entity;

import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 조직의 타입을 지정하는 enum
 */
@RequiredArgsConstructor
public enum OrganizationType {
    UNIVERSITY("대학"),
    COMPANY("회사"),
    HIGH_SCHOOL("고등학교"),
    ETC("기타");

    private final String korean;
}
