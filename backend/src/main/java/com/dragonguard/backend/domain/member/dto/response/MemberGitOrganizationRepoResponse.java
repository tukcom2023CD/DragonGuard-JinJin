package com.dragonguard.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author 김승진
 * @description 멤버 깃허브 조직의 레포지토리 요청에 대한 응답 정보를 갖는 dto 클래스
 */

@Getter
@AllArgsConstructor
public class MemberGitOrganizationRepoResponse {
    private String profileImage;
    private List<String> gitRepos;
}
