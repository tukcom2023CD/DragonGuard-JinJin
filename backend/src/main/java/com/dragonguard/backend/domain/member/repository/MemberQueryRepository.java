package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 DB 조회 접근에 대한 인터페이스
 */

public interface MemberQueryRepository {
    List<MemberRankResponse> findRanking(Pageable pageable);
    Integer findRankingById(UUID id);
    List<MemberRankResponse> findRankingByOrganization(Long organizationId, Pageable pageable);
    Optional<Member> findByGithubId(String githubId);
    String findRefreshTokenById(UUID id);
    Optional<Member> findById(UUID id);
    boolean existsByGithubId(String githubId);
}
