package com.dragonguard.backend.member.dto.response;

import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 상세 조회를 위한 dto
 */

@Getter
@Builder
@AllArgsConstructor
public class MemberDetailResponse {
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
    private List<String> gitOrganizations;
    private List<String> gitRepos;
}
