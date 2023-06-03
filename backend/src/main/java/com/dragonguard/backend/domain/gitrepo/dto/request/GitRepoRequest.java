package com.dragonguard.backend.domain.gitrepo.dto.request;

import lombok.*;

/**
 * @author 김승진
 * @description 깃허브 Repository 요청 정보를 원하는 년도와 함께 담는 dto
 */

@Getter
@ToString // Redis를 위해 넣은 toString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoRequest {
    @Setter
    private String githubToken;
    private String id;
    private Integer year;

    public GitRepoRequest(String id, Integer year) {
        this.id = id;
        this.year = year;
    }
}
