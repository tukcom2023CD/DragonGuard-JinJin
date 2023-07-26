package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;

import java.util.List;

public interface GitRepoMemberService {
    GitRepoMember findByGitRepoAndMemberGithubId(GitRepo gitRepo, String githubId);
    void saveAll(List<GitRepoMemberResponse> result, GitRepo gitRepo);
    Boolean isServiceMember(String githubId);
}
