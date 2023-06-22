package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 커밋 테이블의 DB 접근을 수행하는 로직을 가진 인터페이스
 */

public interface CommitRepository {
    List<Commit> findAllByMember(Member member);
    Commit save(Commit commit);
    Optional<Commit> findById(Long id);
}
