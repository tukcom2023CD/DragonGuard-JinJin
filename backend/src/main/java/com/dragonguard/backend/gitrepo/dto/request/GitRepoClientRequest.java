package com.dragonguard.backend.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoClientRequest {
    @Setter
    private String githubToken;
    private String name;
}
