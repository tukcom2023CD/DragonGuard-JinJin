package com.dragonguard.backend.domain.admin.mapper;

import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 김승진
 * @description 관리자의 조직 로직 관련 dto와 조직을 매핑해주는 Mapper
 */

@Mapper(componentModel = "spring")
public interface AdminMapper {
    @IterableMapping(elementTargetType = AdminOrganizationResponse.class)
    List<AdminOrganizationResponse> toResponseList(final List<Organization> organizations);

    @Mapping(target = "type", source = "organization.organizationType")
    AdminOrganizationResponse toResponse(final Organization organization);
}
