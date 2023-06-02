package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface GitRepoMemberRepository {
    List<GitRepoMember> findAllByGitRepo(GitRepo gitRepo);

    boolean existsByGitRepoAndMember(GitRepo gitRepo, Member member);

    Optional<GitRepoMember> findByGitRepoAndMember(GitRepo gitRepo, Member member);

    Optional<GitRepoMember> findByNameAndMemberName(String gitRepo, String member);

    <S extends GitRepoMember> List<S> saveAll(Iterable<S> entities);

    GitRepoMember save(GitRepoMember gitRepoMember);
}
