package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author 김승진
 * @description 멤버 상세 조회를 위한 dto
 */

@Getter
@Builder
@AllArgsConstructor
public class MemberGitReposAndGitOrganizationsResponse {
    private List<MemberGitOrganizationResponse> gitOrganizations;
    private List<String> gitRepos;
    private String memberProfileImage;
}
