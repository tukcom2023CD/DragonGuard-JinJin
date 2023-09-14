package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;

import java.util.List;

public interface GitRepoMemberService {
    GitRepoMember findByGitRepoAndMemberGithubId(final GitRepo gitRepo, final String githubId);
    void saveAll(final List<GitRepoMemberResponse> result, final GitRepo gitRepo);
    Boolean isServiceMember(final String githubId);
    void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoMemberResponses, final GitRepo gitRepo);
}
