package com.dragonguard.backend.domain.issue.repository;

import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author 김승진
 * @description issue Entity의 DB CRUD를 담당하는 클래스
 */

public interface JpaIssueRepository extends JpaRepository<Issue, Long>, IssueRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Optional<Issue> findByMemberAndYear(Member member, int year);
}
