package com.dragonguard.backend.domain.gitrepo.dto.response;

import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author 김승진
 * @description 레포지토리 관련 응답 정보를 담는 dto 클래스
 */

@Getter
@AllArgsConstructor
public class GitRepoResponse {
    private List<Integer> sparkLine;
    private List<GitRepoMemberResponse> gitRepoMembers;
}
