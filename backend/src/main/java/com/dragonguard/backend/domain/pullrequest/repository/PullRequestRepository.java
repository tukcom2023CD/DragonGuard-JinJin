package com.dragonguard.backend.domain.pullrequest.repository;

import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.repository.ContributionRepository;

/**
 * @author 김승진
 * @description Pull Request에 대한 DB 접근 로직을 갖는 인터페이스
 */

public interface PullRequestRepository extends ContributionRepository<PullRequest, Long> {}
