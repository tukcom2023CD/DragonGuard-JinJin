package com.dragonguard.backend.pullrequest.repository;

import com.dragonguard.backend.pullrequest.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 PullRequest 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
    boolean existsByGithubIdAndYear(String githubId, Integer year);

    Optional<PullRequest> findByGithubIdAndYear(String githubId, Integer year);

    List<PullRequest> findByGithubId(String githubId);
}
