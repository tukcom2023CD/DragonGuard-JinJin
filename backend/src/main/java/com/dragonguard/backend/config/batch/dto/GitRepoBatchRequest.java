package com.dragonguard.backend.config.batch.dto;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GitRepoBatchRequest {
    private String githubToken;
    private GitRepo gitRepo;
}
