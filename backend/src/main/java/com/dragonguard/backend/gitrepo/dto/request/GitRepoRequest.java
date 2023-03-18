package com.dragonguard.backend.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 김승진
 * @description 깃허브 Repository 요청 정보를 원하는 년도와 함께 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoRequest {
    @Setter
    private String githubToken;
    private String name;
    private Integer year;

    public GitRepoRequest(String name, Integer year) {
        this.name = name;
        this.year = year;
    }
}
