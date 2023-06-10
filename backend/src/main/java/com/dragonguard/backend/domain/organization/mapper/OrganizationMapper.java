package com.dragonguard.backend.domain.organization.mapper;

import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import org.mapstruct.Mapper;

/**
 * @author 김승진
 * @description 조직 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    Organization toEntity(final OrganizationRequest organizationRequest);
    OrganizationResponse toResponse(final Organization organization);
}
