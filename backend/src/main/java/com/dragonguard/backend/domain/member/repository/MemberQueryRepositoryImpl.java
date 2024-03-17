package com.dragonguard.backend.domain.member.repository;

import static com.dragonguard.backend.domain.member.entity.QMember.member;

import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 멤버 DB 조회 접근에 대한 구현체
 */
@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberOrderConverter memberOrderConverter;
    private final MemberQDtoFactory qDtoFactory;

    @Override
    public List<MemberRankResponse> findRanking(final Pageable pageable) {
        return jpaQueryFactory
                .select(qDtoFactory.qMemberRankResponse())
                .from(member)
                .where(member.walletAddress.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberOrderConverter.convert(pageable.getSort()))
                .fetch();
    }

    @Override
    public Integer findRankingById(final UUID id) {
        return jpaQueryFactory
                        .selectFrom(member)
                        .where(
                                member.walletAddress
                                        .isNotNull()
                                        .and(
                                                member.sumOfTokens.gt(
                                                        JPAExpressions.select(member.sumOfTokens)
                                                                .from(member)
                                                                .where(member.id.eq(id)))))
                        .fetch()
                        .size()
                + 1;
    }

    @Override
    public List<MemberRankResponse> findRankingByOrganization(
            final Long organizationId, final Pageable pageable) {
        return jpaQueryFactory
                .select(qDtoFactory.qMemberRankResponse())
                .from(member)
                .where(
                        member.walletAddress
                                .isNotNull()
                                .and(member.organization.id.eq(organizationId))
                                .and(member.authStep.eq(AuthStep.ALL)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberOrderConverter.convert(pageable.getSort()))
                .fetch();
    }

    @Override
    public Optional<Member> findByGithubId(final String githubId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(member)
                        .where(member.githubId.eq(githubId))
                        .fetchFirst());
    }

    @Override
    public String findRefreshTokenById(final UUID id) {
        return jpaQueryFactory
                .select(member.refreshToken)
                .from(member)
                .where(member.id.eq(id))
                .fetchFirst();
    }

    @Override
    public boolean existsByGithubId(final String githubId) {
        return jpaQueryFactory.selectFrom(member).where(member.githubId.eq(githubId)).fetchFirst()
                != null;
    }
}
