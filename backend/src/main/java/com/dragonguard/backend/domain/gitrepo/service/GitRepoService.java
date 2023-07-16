package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;

import java.util.Set;

public interface GitRepoService {
    TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request);
    TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest request);
    GitRepo getEntityByName(String name);
    void saveAll(final Set<GitRepo> gitRepos);
    boolean gitRepoExistsByName(final String name);
}
