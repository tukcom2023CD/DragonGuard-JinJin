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
    private final UUID id;
    private final String name;
    private final String githubId;
    private final Long tokens;
    private final Tier tier;
    private final String profileImage;

    @QueryProjection
    public MemberRankResponse(
            UUID id, String name, String githubId, Long tokens, Tier tier, String profileImage) {
        this.id = id;
        this.name = name;
        this.githubId = githubId;
        this.tokens = tokens;
        this.tier = tier;
        this.profileImage = profileImage;
    }
}
