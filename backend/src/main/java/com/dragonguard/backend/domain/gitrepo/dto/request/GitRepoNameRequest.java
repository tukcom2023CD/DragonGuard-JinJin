package com.dragonguard.backend.domain.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 Repository 요청 정보를 담는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoNameRequest {
    private String name;
}
