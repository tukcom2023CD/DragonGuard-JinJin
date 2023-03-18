package com.dragonguard.backend.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoClientRequest {
    private String githubToken;
    private String name;
}
