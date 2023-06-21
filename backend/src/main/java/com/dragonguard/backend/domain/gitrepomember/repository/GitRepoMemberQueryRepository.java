package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface GitRepoMemberQueryRepository {
    Optional<GitRepoMember> findByGitRepoAndMember(GitRepo gitRepo, Member member);
    Optional<GitRepoMember> findByNameAndMemberGithubId(String name, String githubId);
}
