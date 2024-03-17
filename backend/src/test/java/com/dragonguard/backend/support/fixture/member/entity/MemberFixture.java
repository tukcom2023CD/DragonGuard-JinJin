package com.dragonguard.backend.support.fixture.member.entity;

import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberFixture {
    OHKSJ77("Kim", "ohksj77", "123412344", "https://github", Role.ROLE_ADMIN, AuthStep.ALL),
    POSITE("Shim", "posite", "123412234", "https://github", Role.ROLE_ADMIN, AuthStep.ALL),
    SAMMUELWOOJAE(
            "Kwan", "Sammuelwoojae", "123412354", "https://github", Role.ROLE_ADMIN, AuthStep.ALL),
    HJ39("Jeong", "HJ39", "123041234", "https://github", Role.ROLE_ADMIN, AuthStep.ALL);

    String name;
    String githubId;
    String walletAddress;
    String profileImage;
    Role role;
    AuthStep authStep;

    public Member toEntity() {
        return new Member(name, githubId, walletAddress, profileImage, role, authStep);
    }
}
