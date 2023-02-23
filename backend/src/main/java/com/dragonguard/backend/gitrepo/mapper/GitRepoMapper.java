package com.dragonguard.backend.gitrepo.mapper;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import org.springframework.stereotype.Component;

@Component
public class GitRepoMapper {
    public GitRepo toEntity(GitRepoRequest gitRepoRequest) {
        return GitRepo.builder()
                .name(gitRepoRequest.getName())
                .build();
    }
}
