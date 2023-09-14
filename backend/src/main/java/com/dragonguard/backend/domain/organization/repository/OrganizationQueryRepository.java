package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.dto.response.RelatedRankWithMemberResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB 조회 수행의 규격을 정의하는 인터페이스
 */

public interface OrganizationQueryRepository {
    List<OrganizationResponse> findRanking(final Pageable pageable);

    List<OrganizationResponse> findRankingByType(final OrganizationType type, final Pageable pageable);

    List<OrganizationResponse> findByTypeAndSearchWord(final OrganizationType type, final String name, final Pageable pageable);

    RelatedRankWithMemberResponse findRankingByMemberId(final UUID memberId, final String githubId);

    List<Organization> findAllByOrganizationStatus(final OrganizationStatus organizationStatus, final Pageable pageable);
}
