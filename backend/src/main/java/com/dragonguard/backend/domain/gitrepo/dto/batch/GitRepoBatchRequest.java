package com.dragonguard.backend.domain.gitrepo.dto.batch;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GitRepoBatchRequest {
    private String githubToken;
    private GitRepo gitRepo;
}
