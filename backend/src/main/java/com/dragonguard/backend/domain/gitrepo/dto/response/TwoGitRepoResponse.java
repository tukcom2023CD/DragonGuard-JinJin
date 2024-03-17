package com.dragonguard.backend.domain.gitrepo.dto.response;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoCompareResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 김승진
 * @description 깃허브 두 Repository 비교 응답 정보를 담는 dto
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TwoGitRepoResponse {
    private GitRepoCompareResponse firstRepo;
    private GitRepoCompareResponse secondRepo;
}
