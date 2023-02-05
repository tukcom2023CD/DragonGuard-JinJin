package com.dragonguard.backend.support.fixture.member.dto;

import com.dragonguard.backend.member.dto.request.MemberRequest;

public enum MemberRequestFixture {
    SAMPLE1(
            "ohksj77"
    ),
    SAMPLE2(
            "HJ39"
    ),
    SAMPLE3(
            "posite"
    ),
    SAMPLE4(
            "Sammuelwoojae"
    );

    private String githubId;

    MemberRequestFixture(String githubId) {
        this.githubId = githubId;
    }

    public MemberRequest toMemberRequest() {
        return new MemberRequest(githubId);
    }
}
