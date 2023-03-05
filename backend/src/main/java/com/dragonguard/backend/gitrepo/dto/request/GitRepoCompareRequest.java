package com.dragonguard.backend.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 Repository 비교 요청 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoCompareRequest {
    private String firstRepo;
    private String secondRepo;
}
