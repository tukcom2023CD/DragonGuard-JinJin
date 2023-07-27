package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaGitRepoRepository extends JpaRepository<GitRepo, Long>, GitRepoRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT gr FROM GitRepo gr WHERE gr.name = :name")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1500")})
    Optional<GitRepo> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1500")})
    boolean existsByName(String name);

    @Query("SELECT gr FROM GitRepo gr LEFT JOIN FETCH gr.gitRepoMembers grm LEFT JOIN FETCH grm.member")
    Page<GitRepo> findAllWithMember(Pageable pageable);
    @Query("SELECT gr FROM GitRepo gr LEFT JOIN FETCH gr.gitRepoMembers grm LEFT JOIN FETCH grm.member WHERE gr.id = :id")
    Optional<GitRepo> findByIdWithGitRepoMember(Long id);
}
