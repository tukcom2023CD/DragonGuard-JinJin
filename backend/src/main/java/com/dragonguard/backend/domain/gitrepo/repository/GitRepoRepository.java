package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 레포지토리 관련 DB 접근 로직을 갖는 인터페이스
 */

public interface GitRepoRepository {
    GitRepo save(final GitRepo gitRepo);
    <S extends GitRepo> List<S> saveAll(final Iterable<S> entities);
    Optional<GitRepo> findByName(final String name);
    Optional<GitRepo> findById(final Long id);
    Optional<GitRepo> findByIdWithGitRepoMember(final Long id);
    boolean existsByName(final String name);
    List<GitRepo> findAll();
    Page<GitRepo> findAllWithMember(final Pageable pageable);
}
