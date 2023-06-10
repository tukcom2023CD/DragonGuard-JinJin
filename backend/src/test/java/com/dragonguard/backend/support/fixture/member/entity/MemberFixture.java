package com.dragonguard.backend.support.fixture.member.entity;

import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberFixture {
    SAMPLE1("Kim", "ohksj77", "12341234", "https://github", Role.ROLE_ADMIN, AuthStep.ALL),
    SAMPLE2("Shim", "posite", "12341234", "https://github", Role.ROLE_ADMIN, AuthStep.ALL),
    SAMPLE3("Kwan", "Sammuelwoojae", "12341234", "https://github", Role.ROLE_ADMIN, AuthStep.ALL),
    SAMPLE4("Jeong", "HJ39", "12341234", "https://github", Role.ROLE_ADMIN, AuthStep.ALL);

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
