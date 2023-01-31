package com.dragonguard.backend.support.fixture.member.dto;

import com.dragonguard.backend.member.dto.request.MemberRequest;

public enum MemberRequestFixture {
    SAMPLE1(
            "김승진",
            "ohksj77"
    ),
    SAMPLE2(
            "정호진",
            "HJ39"
    ),
    SAMPLE3(
            "심영수",
            "posite"
    ),
    SAMPLE4(
            "김관용",
            "Sammuelwoojae"
    );

    private String name;
    private String githubId;

    MemberRequestFixture(String name, String githubId) {
        this.name = name;
        this.githubId = githubId;
    }

    public MemberRequest toMemberRequest() {
        return new MemberRequest(name, githubId);
    }
}
