package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dragonguard.backend.domain.member.entity.QMember.member;


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
    public List<MemberRankResponse> findRanking(Pageable pageable) {
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
    public Integer findRankingById(UUID id) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(member.walletAddress.isNotNull().and(member.sumOfTokens.gt(
                        JPAExpressions
                                .select(member.sumOfTokens).from(member).where(member.id.eq(id)))))
                .fetch().size() + 1;
    }

    @Override
    public List<MemberRankResponse> findRankingByOrganization(Long organizationId, Pageable pageable) {
        return jpaQueryFactory
                .select(qDtoFactory.qMemberRankResponse())
                .from(member)
                .where(member.walletAddress.isNotNull().and(member.organization.id.eq(organizationId)).and(member.authStep.eq(AuthStep.ALL)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberOrderConverter.convert(pageable.getSort()))
                .fetch();
    }

    @Override
    public Optional<Member> findByGithubId(String githubId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(member)
                .where(member.githubId.eq(githubId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst());
    }

    @Override
    public String findRefreshTokenById(UUID id) {
        return jpaQueryFactory
                .select(member.refreshToken)
                .from(member)
                .where(member.id.eq(id))
                .fetchFirst();
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(member)
                .where(member.id.eq(id))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst());
    }

    @Override
    public boolean existsByGithubId(String githubId) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(member.githubId.eq(githubId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst() != null;
    }
}
