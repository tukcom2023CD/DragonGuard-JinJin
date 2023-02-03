package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberQueryRepository {
    List<Member> findRanking(Pageable pageable);
}
