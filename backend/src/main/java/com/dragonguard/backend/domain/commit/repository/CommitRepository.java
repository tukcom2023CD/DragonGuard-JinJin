package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;

import java.util.List;

public interface CommitRepository {
    List<Commit> findCommitsByGithubId(String githubId);

    Commit save(Commit commit);
}
