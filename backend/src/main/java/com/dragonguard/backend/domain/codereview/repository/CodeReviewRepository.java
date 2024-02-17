package com.dragonguard.backend.domain.codereview.repository;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.global.template.repository.ContributionRepository;

/**
 * @author 김승진
 * @description 코드리뷰 관련 db와 연계할 메소드들을 가진 인터페이스
 */
public interface CodeReviewRepository extends ContributionRepository<CodeReview, Long> {}
