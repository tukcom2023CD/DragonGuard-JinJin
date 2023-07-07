package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author 김승진
 * @description 커밋 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaCommitRepository extends JpaRepository<Commit, Long>, CommitRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Optional<Commit> findByMemberAndYear(Member member, int year);
}
