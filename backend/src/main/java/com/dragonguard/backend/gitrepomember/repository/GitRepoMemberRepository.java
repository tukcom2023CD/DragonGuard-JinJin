package com.dragonguard.backend.gitrepomember.repository;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GitRepoMemberRepository extends JpaRepository<GitRepoMember, Long> {
    List<GitRepoMember> findAllByGitRepo(GitRepo gitRepo);
    boolean existsByGitRepoAndMember(GitRepo gitRepo, Member member);
    GitRepoMember findByGitRepoAndMember(GitRepo gitRepo, Member member);
    @Query(value = "SELECT grm FROM GitRepoMember grm, GitRepo gr WHERE gr.name = :gitRepo AND grm.member.githubId = :member")
    GitRepoMember findByNameAndMemberName(String gitRepo, String member);
}
