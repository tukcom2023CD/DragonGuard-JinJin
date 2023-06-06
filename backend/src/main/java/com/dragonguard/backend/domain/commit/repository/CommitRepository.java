package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;

import java.util.List;
import java.util.Optional;

public interface CommitRepository {
    List<Commit> findAllByGithubId(String githubId);
    Commit save(Commit commit);
    Optional<Commit> findById(Long id);
    void deleteAll(Iterable<? extends Commit> entities);
}
