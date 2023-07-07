package com.dragonguard.backend.domain.codereview.repository;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.Optional;

/**
 * @author 김승진
 * @description 코드리뷰 관련 db와 연계할 메소드들을 가진 인터페이스
 */

public interface CodeReviewRepository {
    CodeReview save(CodeReview commit);
    Optional<CodeReview> findById(Long id);
    Optional<CodeReview> findByMemberAndYear(Member member, int year);
    boolean existsByMemberAndYear(Member member, int year);
}
