package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface JpaGitRepoRepository extends JpaRepository<GitRepo, Long>, GitRepoRepository {
    @Query("SELECT DISTINCT gr FROM GitRepo gr JOIN FETCH gr.gitRepoMembers grm JOIN FETCH grm.member m WHERE m.githubId = :githubId")
    List<GitRepo> findByGithubId(String githubId);
}
