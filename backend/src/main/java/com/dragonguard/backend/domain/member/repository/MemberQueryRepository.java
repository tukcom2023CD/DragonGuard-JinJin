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
    List<MemberRankResponse> findRanking(final Pageable pageable);
    Integer findRankingById(final UUID id);
    List<MemberRankResponse> findRankingByOrganization(final Long organizationId, final Pageable pageable);
    Optional<Member> findByGithubId(final String githubId);
    String findRefreshTokenById(final UUID id);
    boolean existsByGithubId(final String githubId);
}
