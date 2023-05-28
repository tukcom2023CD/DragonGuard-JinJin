package com.dragonguard.backend.domain.pullrequest.repository;

import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;

import java.util.List;
import java.util.Optional;

public interface PullRequestRepository {
    boolean existsByGithubIdAndYear(String githubId, Integer year);

    Optional<PullRequest> findByGithubIdAndYear(String githubId, Integer year);

    List<PullRequest> findByGithubId(String githubId);

    PullRequest save(PullRequest pullRequest);
}
