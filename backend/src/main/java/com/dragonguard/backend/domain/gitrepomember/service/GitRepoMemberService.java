package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.template.service.EntityLoader;

import java.util.List;
import java.util.Set;

public interface GitRepoMemberService extends EntityLoader<GitRepoMember, Long> {
    GitRepoMember findByGitRepoAndMemberGithubId(final GitRepo gitRepo, final String githubId);
    void saveAllIfNotExists(final List<GitRepoMemberResponse> result, final GitRepo gitRepo);
    Boolean isServiceMember(final String githubId);
    void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoMemberResponses, final GitRepo gitRepo);
    void saveAllIfNotExists(final Member member, final Set<GitRepo> gitRepos);
}
