package com.dragonguard.backend.domain.organization.mapper;

import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.global.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author 김승진
 * @description 조직 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface OrganizationMapper extends EntityMapper {
    Organization toEntity(final OrganizationRequest organizationRequest);

    @Mapping(target = "tokenSum", expression = "java(organization.getSumOfMemberTokens())")
    OrganizationResponse toResponse(final Organization organization);
}
