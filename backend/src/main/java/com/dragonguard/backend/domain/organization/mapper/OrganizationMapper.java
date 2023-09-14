package com.dragonguard.backend.domain.organization.mapper;

import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

/**
 * @author 김승진
 * @description 조직 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrganizationMapper {
    Organization toEntity(final OrganizationRequest organizationRequest);

    @Mapping(target = "tokenSum", expression = "java(organization.getSumOfMemberTokens())")
    OrganizationResponse toResponse(final Organization organization);
}
