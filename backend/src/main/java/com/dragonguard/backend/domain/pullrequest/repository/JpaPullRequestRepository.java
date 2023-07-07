package com.dragonguard.backend.domain.pullrequest.repository;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 PullRequest 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaPullRequestRepository extends JpaRepository<PullRequest, Long>, PullRequestRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Optional<PullRequest> findByMemberAndYear(Member member, int year);
}
