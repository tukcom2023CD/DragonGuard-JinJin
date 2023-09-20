package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * @author 김승진
 * @description 멤버 깃허브 조직과 레포지토리 응답 정보를 갖는 dto 클래스
 */

@Getter
@Builder
@AllArgsConstructor
public class MemberGitReposAndGitOrganizationsResponse {
    private Set<MemberGitOrganizationResponse> gitOrganizations;
    private Set<String> gitRepos;
    private String memberProfileImage;
}
