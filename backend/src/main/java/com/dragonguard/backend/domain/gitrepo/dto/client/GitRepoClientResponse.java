package com.dragonguard.backend.domain.gitrepo.dto.client;

import lombok.*;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 Github REST API의 응답 정보를 담는 dto
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoClientResponse {
    private String fullName;
    private Integer forksCount;
    private Integer stargazersCount;
    private Integer watchersCount;
    private Integer openIssuesCount;
    @Setter
    private Integer closedIssuesCount;
    private Integer subscribersCount;
}
