package com.dragonguard.backend.domain.issue.repository;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.global.repository.ContributionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 이슈 관련 DB 접근을 처리하는 로직을 갖는 인터페이스
 */

@Repository
public interface IssueRepository extends ContributionRepository<Issue, Long> {}
