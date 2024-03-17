package com.dragonguard.backend.domain.member.dto.response;

import com.dragonguard.backend.domain.member.entity.Tier;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 랭킹 응답 정보를 담는 dto
 */
@Getter
@NoArgsConstructor
public class MemberRankResponse {
    private UUID id;
    private String name;
    private String githubId;
    private Long tokens;
    private Tier tier;
    private String profileImage;

    @QueryProjection
    public MemberRankResponse(
            final UUID id,
            final String name,
            final String githubId,
            final Long tokens,
            final Tier tier,
            final String profileImage) {
        this.id = id;
        this.name = name;
        this.githubId = githubId;
        this.tokens = tokens;
        this.tier = tier;
        this.profileImage = profileImage;
    }
}
