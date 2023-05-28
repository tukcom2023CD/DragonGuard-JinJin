package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 커밋 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface JpaCommitRepository extends JpaRepository<Commit, Long>, CommitRepository {
}
