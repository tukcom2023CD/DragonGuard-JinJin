package com.dragonguard.backend.support.fixture.member.dto;

import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberRequestFixture {
    OHKSJ77(
            "ohksj77"
    ),
    HJ39(
            "HJ39"
    ),
    POSITE(
            "posite"
    ),
    SAMMUELWOOJAE(
            "Sammuelwoojae"
    );

    private String githubId;

    public MemberRequest toMemberRequest() {
        return new MemberRequest(githubId);
    }
}
