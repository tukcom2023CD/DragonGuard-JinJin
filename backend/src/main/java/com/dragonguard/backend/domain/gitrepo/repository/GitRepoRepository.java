package com.dragonguard.backend.domain.gitrepo.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;

import java.util.List;
import java.util.Optional;

public interface GitRepoRepository {
    Optional<GitRepo> findByName(String name);

    boolean existsByName(String name);

    List<GitRepo> findByGithubId(String githubId);

//    GitRepo save(GitRepo gitRepo);

//    List<GitRepo> saveAll(Set<GitRepo> gitRepos);
}
