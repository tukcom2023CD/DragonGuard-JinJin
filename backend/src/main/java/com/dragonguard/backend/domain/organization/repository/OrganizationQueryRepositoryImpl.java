package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.dto.response.RelatedRankWithMemberResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dragonguard.backend.domain.member.entity.QMember.member;
import static com.dragonguard.backend.domain.organization.entity.QOrganization.organization;

/**
 * @author 김승진
 * @description Organization DB 조회 접근에 대한 구현체
 */

@Repository
@RequiredArgsConstructor
public class OrganizationQueryRepositoryImpl implements OrganizationQueryRepository {
    private static final Long FROM_FIRST_OFFSET = 0L;
    private static final Long FROM_TWO_UPPER_OFFSET = 2L;
    private static final Long FROM_THREE_UPPER_OFFSET = 3L;
    private static final int FIRST_RANK = 1;
    private static final int SECOND_RANK = 2;
    private static final int RELATED_RANK_SIZE = 3;
    private static final int LIST_INDEX_MINUS_UNIT = 1;
    private final JPAQueryFactory jpaQueryFactory;
    private final OrganizationQDtoFactory organizationQDtoFactory;

    @Override
    public List<OrganizationResponse> findRanking(final Pageable pageable) {
        return jpaQueryFactory
                .select(organizationQDtoFactory.qOrganizationResponse())
                .from(organization, member)
                .where(organization.organizationStatus.eq(OrganizationStatus.ACCEPTED)
                        .and(member.authStep.eq(AuthStep.ALL)))
                .leftJoin(organization.members, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(organization.sumOfMemberTokens.desc())
                .distinct()
                .fetch();
    }

    @Override
    public List<OrganizationResponse> findRankingByType(final OrganizationType type, final Pageable pageable) {
        return jpaQueryFactory
                .select(organizationQDtoFactory.qOrganizationResponse())
                .from(organization, member)
                .leftJoin(organization.members, member)
                .where(organization.organizationStatus.eq(OrganizationStatus.ACCEPTED)
                        .and(organization.organizationType.eq(type).and(member.authStep.eq(AuthStep.ALL))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(organization.sumOfMemberTokens.desc())
                .distinct()
                .fetch();
    }

    @Override
    public List<OrganizationResponse> findByTypeAndSearchWord(final OrganizationType type, final String name, final Pageable pageable) {
        return jpaQueryFactory
                .select(organizationQDtoFactory.qOrganizationResponse())
                .from(organization)
                .where(organization.organizationStatus.eq(OrganizationStatus.ACCEPTED)
                        .and(organization.organizationType.eq(type).and(organization.name.containsIgnoreCase(name))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();
    }

    @Override
    public RelatedRankWithMemberResponse findRankingByMemberId(final UUID memberId, final String githubId) {
        return getRelatedRankWithMemberResponse(jpaQueryFactory
                .select(member)
                .from(member, organization)
                .leftJoin(member.organization, organization)
                .on(organization.organizationStatus.eq(OrganizationStatus.ACCEPTED))
                .where(member.sumOfTokens.gt(
                        JPAExpressions
                                .select(member.sumOfTokens)
                                .from(member)
                                .leftJoin(member.organization, organization)
                                .where(member.id.eq(memberId)
                                        .and(organization.id.eq(member.organization.id))))
                        .and(organization.organizationStatus.eq(OrganizationStatus.ACCEPTED)
                                .and(member.authStep.eq(AuthStep.ALL))))
                .distinct()
                .fetch().size() + 1, githubId);
    }

    private RelatedRankWithMemberResponse getRelatedRankWithMemberResponse(final int rank, final String githubId) {
        final Long organizationId = getOrganizationIdByGithubId(githubId);

        final List<String> relatedRank = jpaQueryFactory
                .select(member.githubId, member.id, member.sumOfTokens)
                .from(member)
                .leftJoin(member.organization, organization)
                .on(organization.organizationStatus.eq(OrganizationStatus.ACCEPTED))
                .where(member.authStep.eq(AuthStep.ALL).and(member.organization.id.eq(organizationId)))
                .orderBy(member.sumOfTokens.desc())
                .distinct()
                .offset(getOffset(rank))
                .limit(RELATED_RANK_SIZE)
                .fetch().stream().map(t -> t.get(member.githubId)).collect(Collectors.toList());
        return new RelatedRankWithMemberResponse(rank, isLast(githubId, relatedRank), relatedRank);
    }

    private Long getOrganizationIdByGithubId(final String githubId) {
        return jpaQueryFactory
                .select(member.organization.id)
                .from(member)
                .where(member.githubId.eq(githubId))
                .fetchFirst();
    }

    private boolean isLast(final String githubId, final List<String> relatedRank) {
        return relatedRank.isEmpty() || relatedRank.get(relatedRank.size() - LIST_INDEX_MINUS_UNIT).equals(githubId);
    }

    @Override
    public List<Organization> findAllByOrganizationStatus(final OrganizationStatus organizationStatus, final Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(organization)
                .where(organization.organizationStatus.eq(organizationStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private long getOffset(final int rank) {
        final int size = jpaQueryFactory
                .select(member.id)
                .from(member, organization)
                .leftJoin(organization.members, member)
                .on(member.organization.id.eq(organization.id))
                .distinct()
                .fetch()
                .size();

        return getOffsetWithSize(rank, size);
    }

    private long getOffsetWithSize(final int rank, final int size) {
        if (rank == FIRST_RANK) {
            return FROM_FIRST_OFFSET;
        }
        return getNotFirstRankOffset(rank, size);
    }

    private long getNotFirstRankOffset(final int rank, final int size) {
        if (isLastRank(rank, size)) {
            return getLastRankOffset(rank);
        }
        return rank - FROM_TWO_UPPER_OFFSET;
    }

    private boolean isLastRank(final int rank, final int size) {
        return size == rank;
    }

    private long getLastRankOffset(final int rank) {
        if (isTwoRankers(rank)) {
            return FROM_FIRST_OFFSET;
        }
        return rank - FROM_THREE_UPPER_OFFSET;
    }

    private boolean isTwoRankers(final int rank) {
        return rank == SECOND_RANK;
    }
}
