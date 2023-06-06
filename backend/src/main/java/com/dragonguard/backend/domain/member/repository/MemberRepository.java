package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.entity.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Optional<Member> findByGithubId(String githubId);
    String findRefreshTokenById(UUID id);
    void updateRefreshToken(UUID id, String token);
    Optional<Member> findById(UUID id);
    Member save(Member member);
}
