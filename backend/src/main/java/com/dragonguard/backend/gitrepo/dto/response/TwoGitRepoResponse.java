package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 깃허브 두 Repository 비교 응답 정보를 담는 dto
 */

@Getter
@AllArgsConstructor
public class TwoGitRepoResponse {
    private GitRepoResponse firstRepo;
    private GitRepoResponse secondRepo;
}
