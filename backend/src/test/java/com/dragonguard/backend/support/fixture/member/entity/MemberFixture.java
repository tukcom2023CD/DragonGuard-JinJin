package com.dragonguard.backend.support.fixture.member.entity;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;

public enum MemberFixture {
    SAMPLE1("Kim", "ohksj77", new Commit(2023, 100, "ohksj77"), "12341234", "https://github", Role.ROLE_USER, AuthStep.ALL),
    SAMPLE2("Shim", "posite", new Commit(2023, 100, "posite"), "12341234", "https://github", Role.ROLE_USER, AuthStep.ALL),
    SAMPLE3("Kwan", "Sammuelwoojae", new Commit(2023, 100, "Sammuelwoojae"), "12341234", "https://github", Role.ROLE_USER, AuthStep.ALL),
    SAMPLE4("Jeong", "HJ39", new Commit(2023, 100, "HJ39"), "12341234", "https://github", Role.ROLE_USER, AuthStep.ALL);

    String name;
    String githubId;
    Commit commit;
    String walletAddress;
    String profileImage;
    Role role;
    AuthStep authStep;

    MemberFixture(String name, String githubId, Commit commit, String walletAddress, String profileImage, Role role, AuthStep authStep) {
        this.name = name;
        this.githubId = githubId;
        this.commit = commit;
        this.walletAddress = walletAddress;
        this.profileImage = profileImage;
        this.role = role;
        this.authStep = authStep;
    }

    public Member toMember() {
        return new Member(name, githubId, commit, walletAddress, profileImage, role, authStep);
    }
}
