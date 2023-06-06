package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 김승진
 * @description GitRepoMember 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaGitRepoMemberRepository extends JpaRepository<GitRepoMember, Long>, GitRepoMemberRepository {

    @Query(value = "SELECT grm FROM GitRepoMember grm, GitRepo gr WHERE gr.id = :gitRepo AND grm.member.githubId = :member")
    Optional<GitRepoMember> findByNameAndMemberName(String gitRepo, String member);
}
