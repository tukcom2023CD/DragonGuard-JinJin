package com.dragonguard.backend.domain.member.entity;

/**
 * @author 김승진
 * @description 멤버 인증 단계를 담는 enum
 */

public enum AuthStep {
    NONE, GITHUB_ONLY, GITHUB_AND_KLIP, ALL
}
