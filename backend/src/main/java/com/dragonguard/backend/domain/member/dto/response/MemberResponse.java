package com.dragonguard.backend.domain.member.dto.response;

import com.dragonguard.backend.domain.member.entity.Tier;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 응답 정보를 담는 dto
 */
@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {
    private UUID id;
    private String name;
    private String githubId;
    private Integer commits;
    private Integer issues;
    private Integer pullRequests;
    private Integer reviews;
    private Tier tier;
    private AuthStep authStep;
    private String profileImage;
    private Integer rank;
    private Integer organizationRank;
    private Long tokenAmount;
    private String organization;
    private String blockchainUrl;
}
