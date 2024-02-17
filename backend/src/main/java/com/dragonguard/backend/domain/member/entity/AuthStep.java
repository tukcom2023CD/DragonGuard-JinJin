package com.dragonguard.backend.domain.member.entity;

import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 멤버 인증 단계를 담는 enum
 */
@RequiredArgsConstructor
public enum AuthStep {
    NONE("인증 미완료"),
    GITHUB_ONLY("깃허브 인증 완료"),
    GITHUB_AND_KLIP("깃허브 및 클립 인증 완료"),
    ALL("모든 인증 완료");

    private final String korean;

    public boolean isGithubOnly() {
        return this == GITHUB_ONLY;
    }

    public boolean isNone() {
        return this == NONE;
    }

    public boolean isServiceMemberAuthStep() {
        return this != GITHUB_ONLY && this != NONE;
    }

    public boolean isAll() {
        return this == ALL;
    }
}
