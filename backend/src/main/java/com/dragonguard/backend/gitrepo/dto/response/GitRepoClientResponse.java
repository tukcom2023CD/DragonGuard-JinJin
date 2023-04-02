package com.dragonguard.backend.gitrepo.dto.response;

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
    private String full_name;
    private Integer forks_count;
    private Integer stargazers_count;
    private Integer watchers_count;
    private Integer open_issues_count;
    @Setter
    private Integer closed_issues_count;
    private Integer subscribers_count;
}
