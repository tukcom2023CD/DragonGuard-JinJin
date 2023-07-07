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
    private final JPAQueryFactory jpaQueryFactory;
    private final OrganizationQDtoFactory organizationQDtoFactory;

    @Override
    public List<OrganizationResponse> findRanking(Pageable pageable) {
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
    public List<OrganizationResponse> findRankingByType(OrganizationType type, Pageable pageable) {
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
    public List<OrganizationResponse> findByTypeAndSearchWord(OrganizationType type, String name, Pageable pageable) {
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
    public RelatedRankWithMemberResponse findRankingByMemberId(UUID memberId) {
        return getRelatedRankWithMemberResponse(jpaQueryFactory
                .select(member)
                .from(member, organization)
                .leftJoin(organization.members, member)
                .on(member.organization.id.eq(organization.id))
                .where(member.sumOfTokens.gt(
                        JPAExpressions
                                .select(member.sumOfTokens).from(member).where(member.id.eq(memberId))))
                .distinct()
                .fetch().size() + 1);
    }

    private RelatedRankWithMemberResponse getRelatedRankWithMemberResponse(int rank) {
        Boolean isLast = false;
        long offset = getOffset(rank, isLast);
        return new RelatedRankWithMemberResponse(rank, isLast, jpaQueryFactory
                .select(member.githubId, member.id, member.sumOfTokens)
                .from(member, organization)
                .leftJoin(organization.members, member)
                .on(member.organization.id.eq(organization.id))
                .orderBy(member.sumOfTokens.desc())
                .distinct()
                .offset(offset)
                .limit(3)
                .fetch().stream().map(t -> t.get(member.githubId)).collect(Collectors.toList()));
    }

    @Override
    public List<Organization> findAllByOrganizationStatus(OrganizationStatus organizationStatus, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(organization)
                .where(organization.organizationStatus.eq(organizationStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private long getOffset(int rank, Boolean isLast) {
        int size = jpaQueryFactory
                .select(member.id)
                .from(member, organization)
                .leftJoin(organization.members, member)
                .on(member.organization.id.eq(organization.id))
                .distinct()
                .fetch()
                .size();

        if (rank == 1) {
            if (size == 1) {
                isLast = true;
            }
            return 0L;
        }
        if (size == rank) {
            if (rank == 2) {
                isLast = true;
                return 0L;
            }
            return rank - 3L;
        }
        return rank - 2L;
    }
}
