package com.dragonguard.backend.domain.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RelatedRankWithMemberResponse {
    private Integer organizationRank;
    private Boolean isLast;
    private List<String> memberGithubIds;
}
