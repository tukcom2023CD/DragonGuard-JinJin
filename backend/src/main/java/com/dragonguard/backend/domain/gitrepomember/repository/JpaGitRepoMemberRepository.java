package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 김승진
 * @description GitRepoMember 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaGitRepoMemberRepository extends JpaRepository<GitRepoMember, Long>, GitRepoMemberRepository {}
