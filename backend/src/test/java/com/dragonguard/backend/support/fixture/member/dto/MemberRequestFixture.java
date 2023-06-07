package com.dragonguard.backend.support.fixture.member.dto;

import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
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

    public MemberRequest toMemberRequest() {
        return new MemberRequest(githubId);
    }
}
