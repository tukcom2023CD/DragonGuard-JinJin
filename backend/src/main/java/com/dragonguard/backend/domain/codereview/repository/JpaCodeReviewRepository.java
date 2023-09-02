package com.dragonguard.backend.domain.codereview.repository;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 김승진
 * @description 코드리뷰 관련 db와 연계할 인터페이스
 */

public interface JpaCodeReviewRepository extends JpaRepository<CodeReview, Long>, CodeReviewRepository {}
