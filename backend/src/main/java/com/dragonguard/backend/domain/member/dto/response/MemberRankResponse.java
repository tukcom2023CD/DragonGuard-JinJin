package com.dragonguard.backend.domain.member.dto.response;

import com.dragonguard.backend.domain.member.entity.Tier;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 랭킹 응답 정보를 담는 dto
 */

@Getter
public class MemberRankResponse {
    private UUID id;
    private String name;
    private String githubId;
    private Long tokens;
    private Tier tier;

    @QueryProjection
    public MemberRankResponse(UUID id, String name, String githubId, Long tokens, Tier tier) {
        this.id = id;
        this.name = name;
        this.githubId = githubId;
        this.tokens = tokens;
        this.tier = tier;
    }
}
