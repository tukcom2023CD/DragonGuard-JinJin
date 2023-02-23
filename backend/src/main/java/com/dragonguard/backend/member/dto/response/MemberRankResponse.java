package com.dragonguard.backend.member.dto.response;

import com.dragonguard.backend.member.entity.Tier;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MemberRankResponse {
    private Long id;
    private String name;
    private String githubId;
    private Long tokens;
    private Tier tier;

    @QueryProjection
    public MemberRankResponse(Long id, String name, String githubId, Long tokens, Tier tier) {
        this.id = id;
        this.name = name;
        this.githubId = githubId;
        this.tokens = tokens;
        this.tier = tier;
    }
}
