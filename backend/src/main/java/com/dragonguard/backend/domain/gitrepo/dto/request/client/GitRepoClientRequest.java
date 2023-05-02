package com.dragonguard.backend.domain.gitrepo.dto.request.client;

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
