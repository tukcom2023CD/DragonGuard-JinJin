package com.dragonguard.backend.domain.commit.repository;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface CommitRepository {
    List<Commit> findAllByMember(Member member);
    Commit save(Commit commit);
    Optional<Commit> findById(Long id);
}
