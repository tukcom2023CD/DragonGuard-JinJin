package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;

import java.util.List;
import java.util.Optional;

public interface GitRepoRepository {
    List<GitRepo> findByGithubId(String githubId);
    GitRepo save(GitRepo gitRepo);
    <S extends GitRepo> List<S> saveAll(Iterable<S> entities);
    Optional<GitRepo> findByName(String name);
    Optional<GitRepo> findById(Long id);
    boolean existsByName(String name);
}
