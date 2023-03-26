package com.dragonguard.backend.support.fixture.member.entity;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Role;

public enum MemberFixture {
    SAMPLE1("Kim", "ohksj77", new Commit(2023, 100, "ohksj77"), "12341234", "https://github", Role.ROLE_USER);

    String name;
    String githubId;
    Commit commit;
    String walletAddress;
    String profileImage;
    Role role;

    MemberFixture(String name, String githubId, Commit commit, String walletAddress, String profileImage, Role role) {
        this.name = name;
        this.githubId = githubId;
        this.commit = commit;
        this.walletAddress = walletAddress;
        this.profileImage = profileImage;
        this.role = role;
    }

    public Member toMember() {
        return new Member(name, githubId, commit, walletAddress, profileImage, role);
    }
}
