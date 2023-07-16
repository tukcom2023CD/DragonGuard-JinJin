package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 레포지토리 관련 DB 접근 로직을 갖는 인터페이스
 */

public interface GitRepoRepository {
    GitRepo save(GitRepo gitRepo);
    <S extends GitRepo> List<S> saveAll(Iterable<S> entities);
    Optional<GitRepo> findByName(String name);
    Optional<GitRepo> findById(Long id);
    Optional<GitRepo> findByIdWithGitRepoMember(Long id);
    boolean existsByName(String name);
    List<GitRepo> findAll();
}
