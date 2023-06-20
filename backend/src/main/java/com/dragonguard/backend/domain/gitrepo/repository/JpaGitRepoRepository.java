package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaGitRepoRepository extends JpaRepository<GitRepo, Long>, GitRepoRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT DISTINCT gr FROM GitRepo gr JOIN FETCH gr.gitRepoMembers grm JOIN FETCH grm.member m WHERE m.githubId = :githubId")
    List<GitRepo> findByGithubId(String githubId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT gr FROM GitRepo gr WHERE gr.name = :name")
    Optional<GitRepo> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<GitRepo> findById(Long id);
}
