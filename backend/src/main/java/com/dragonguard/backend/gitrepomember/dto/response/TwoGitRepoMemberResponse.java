package com.dragonguard.backend.gitrepomember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 두 멤버의 기여도 정보를 Github REST API에서 응답을 받아 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwoGitRepoMemberResponse {
    List<GitRepoMemberResponse> firstResult;
    List<GitRepoMemberResponse> secondResult;
}
