package com.dragonguard.backend.domain.pullrequest.repository;

import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 깃허브 PullRequest 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface JpaPullRequestRepository extends JpaRepository<PullRequest, Long>, PullRequestRepository {
}
