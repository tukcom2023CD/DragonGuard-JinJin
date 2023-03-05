package com.dragonguard.backend.commit.repository;

import com.dragonguard.backend.commit.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 김승진
 * @description 커밋 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findCommitsByGithubId(String githubId);
}
