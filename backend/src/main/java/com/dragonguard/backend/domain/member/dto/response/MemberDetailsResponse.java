package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberDetailsResponse {
    private Integer commits;
    private Integer issues;
    private Integer pullRequests;
    private Integer reviews;
    private String profileImage;
    private List<String> gitRepos;
    private String organization;
    private Integer rank;
}
