package com.dragonguard.backend.commit.repository;

import com.dragonguard.backend.commit.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findCommitsByGithubId(String githubId);
}
