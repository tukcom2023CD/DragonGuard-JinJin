package com.dragonguard.backend.domain.gitrepo.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 김승진
 * @description 레포지토리 관련 요청을 위한 정보를 담는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoClientRequest {
    @Setter
    private String githubToken;
    private String name;
}
