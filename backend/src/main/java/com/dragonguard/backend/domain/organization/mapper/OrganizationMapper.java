package com.dragonguard.backend.domain.organization.mapper;

import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) Entity와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class OrganizationMapper {
    public Organization toEntity(OrganizationRequest organizationRequest) {
        return Organization.builder()
                .name(organizationRequest.getName())
                .organizationType(organizationRequest.getOrganizationType())
                .emailEndpoint(organizationRequest.getEmailEndpoint().trim())
                .build();
    }

    public OrganizationResponse toResponse(Organization organization) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .organizationType(organization.getOrganizationType())
                .emailEndpoint(organization.getEmailEndpoint())
                .build();
    }
}
