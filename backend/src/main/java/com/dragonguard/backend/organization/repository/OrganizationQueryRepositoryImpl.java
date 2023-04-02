package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.organization.entity.OrganizationType;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.dragonguard.backend.member.entity.QMember.member;
import static com.dragonguard.backend.organization.entity.QOrganization.organization;

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
                .where(member.authStep.eq(AuthStep.ALL))
                .leftJoin(organization.members, member)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.sumOfTokens.sum().desc())
                .fetch();
    }

    @Override
    public List<OrganizationResponse> findRankingByType(OrganizationType type, Pageable pageable) {
        return jpaQueryFactory
                .select(organizationQDtoFactory.qOrganizationResponse())
                .from(organization, member)
                .leftJoin(organization.members, member)
                .fetchJoin()
                .where(organization.organizationType.eq(type).and(member.authStep.eq(AuthStep.ALL)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.sumOfTokens.sum().desc())
                .fetch();
    }

    @Override
    public List<OrganizationResponse> findByTypeAndSearchWord(OrganizationType type, String name, Pageable pageable) {
        return jpaQueryFactory
                .select(organizationQDtoFactory.qOrganizationResponse())
                .from(organization)
                .where(organization.organizationType.eq(type).and(organization.name.containsIgnoreCase(name)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Integer findRankingByMemberId(UUID memberId) {
        return jpaQueryFactory
                .select(member)
                .from(member, organization)
                .leftJoin(organization.members, member)
                .where(member.organizationDetails.organizationId.eq(organization.id).and(member.sumOfTokens.gt(
                        JPAExpressions
                                .select(member.sumOfTokens).from(member).where(member.id.eq(memberId)))))
                .fetch().size() + 1;
    }
}
