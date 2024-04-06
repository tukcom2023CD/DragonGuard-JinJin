package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 DB와의 CRUD를 담당하는 클래스
 */
public interface JpaGitRepoRepository extends JpaRepository<GitRepo, Long>, GitRepoRepository {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1500")})
    Optional<GitRepo> findByName(final String name);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1500")})
    boolean existsByName(final String name);

    @EntityGraph(attributePaths = {"gitRepoMembers.member"})
    List<GitRepo> findAll();

    @Query(
            "SELECT gr FROM GitRepo gr LEFT JOIN FETCH gr.gitRepoMembers grm LEFT JOIN FETCH grm.member WHERE gr.id = :id")
    Optional<GitRepo> findByIdWithGitRepoMember(final Long id);
}
