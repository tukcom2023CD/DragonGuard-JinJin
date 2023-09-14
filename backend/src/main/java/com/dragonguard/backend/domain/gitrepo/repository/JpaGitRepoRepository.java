package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.global.repository.EntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface JpaGitRepoRepository extends EntityRepository<GitRepo, Long>, GitRepoRepository {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT gr FROM GitRepo gr WHERE gr.name = :name")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1500")})
    Optional<GitRepo> findByName(final String name);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1500")})
    boolean existsByName(final String name);

    @Query("SELECT gr FROM GitRepo gr")
    @EntityGraph(attributePaths = {"gitRepoMembers.member"})
    Page<GitRepo> findAllWithMember(final Pageable pageable);

    @Query("SELECT gr FROM GitRepo gr LEFT JOIN FETCH gr.gitRepoMembers grm LEFT JOIN FETCH grm.member WHERE gr.id = :id")
    Optional<GitRepo> findByIdWithGitRepoMember(final Long id);
}
