package com.dragonguard.backend.domain.organization.entity;

import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 조직의 인증 요청 상태를 나타내는 enum
 */
@RequiredArgsConstructor
public enum OrganizationStatus {
    REQUESTED("승인 요청"),
    ACCEPTED("승인 완료"),
    DENIED("요청 거부");

    private final String korean;

    public boolean isAccepted() {
        return this == ACCEPTED;
    }
}
