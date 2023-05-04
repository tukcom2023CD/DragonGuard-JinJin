package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
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
    List<OrganizationResponse> findRanking(Pageable pageable);

    List<OrganizationResponse> findRankingByType(OrganizationType type, Pageable pageable);

    List<OrganizationResponse> findByTypeAndSearchWord(OrganizationType type, String name, Pageable pageable);

    Integer findRankingByMemberId(UUID memberId);

    List<Organization> findAllByOrganizationStatus(OrganizationStatus organizationStatus, Pageable pageable);
}
