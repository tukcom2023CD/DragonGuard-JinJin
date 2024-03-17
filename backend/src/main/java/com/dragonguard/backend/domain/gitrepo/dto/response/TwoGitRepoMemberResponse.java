package com.dragonguard.backend.domain.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 두 멤버의 기여도 정보를 Github REST API에서 응답을 받아 담는 dto
 */
@Getter
@ToString // redis 때문에 붙인 toString
@NoArgsConstructor
@AllArgsConstructor
public class TwoGitRepoMemberResponse {
    private List<GitRepoMemberResponse> firstResult;
    private List<GitRepoMemberResponse> secondResult;
}
