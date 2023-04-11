package com.dragonguard.backend.issue.repository;

import com.dragonguard.backend.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description issue Entity의 DB CRUD를 담당하는 클래스
 */

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByGithubId(String githubId);

    boolean existsByGithubIdAndYear(String githubId, Integer year);

    Optional<Issue> findByGithubIdAndYear(String githubId, Integer year);
}
