package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.entity.Member;

import java.util.UUID;

public interface MemberRepository {
    void updateRefreshToken(UUID id, String token);
    Member save(Member member);
}
