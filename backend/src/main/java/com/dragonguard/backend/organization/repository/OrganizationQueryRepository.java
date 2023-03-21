package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.organization.entity.OrganizationType;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB 조회 수행의 규격을 정의하는 인터페이스
 */

public interface OrganizationQueryRepository {
    List<OrganizationResponse> findRank(Pageable pageable);

    List<OrganizationResponse> findRankByType(OrganizationType type, Pageable pageable);

    List<OrganizationResponse> findBySearchWord(OrganizationType type, String name, Pageable pageable);
}
