package com.dragonguard.backend.domain.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 김승진
 * @description 깃허브 Repository 요청 정보를 원하는 년도와 함께 담는 dto
 */

@Getter
@ToString // Redis를 위해 넣은 toString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoInfoRequest {
    private String githubToken;
    private String name;
    private Integer year;
}
