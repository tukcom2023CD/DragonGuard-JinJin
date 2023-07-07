package com.dragonguard.backend.domain.codereview.repository;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author 김승진
 * @description 코드리뷰 관련 db와 연계할 인터페이스
 */

public interface JpaCodeReviewRepository extends JpaRepository<CodeReview, Long>, CodeReviewRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Optional<CodeReview> findByMemberAndYear(Member member, int year);
}
