package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.global.template.repository.ContributionRepository;

/**
 * @author 김승진
 * @description 커밋 테이블의 DB 접근을 수행하는 로직을 가진 인터페이스
 */
public interface CommitRepository extends ContributionRepository<Commit, Long> {}
