package com.dragonguard.backend.domain.issue.repository;

import com.dragonguard.backend.domain.issue.entity.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    List<Issue> findByGithubId(String githubId);

    boolean existsByGithubIdAndYear(String githubId, Integer year);

    Optional<Issue> findByGithubIdAndYear(String githubId, Integer year);

    Issue save(Issue issue);
}
