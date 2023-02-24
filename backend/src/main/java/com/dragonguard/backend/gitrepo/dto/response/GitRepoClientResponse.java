package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoClientResponse {
    private String full_name;
    private Integer forks_count;
    private Integer stargazers_count;
    private Integer watchers_count;
    private Integer open_issues_count;
    private Integer subscribers_count;
}
