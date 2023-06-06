package com.dragonguard.backend.domain.admin.mapper;

import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 관리자의 조직 로직 관련 dto와 조직을 매핑해주는 Mapper
 */

@Component
public class AdminMapper {

    public List<AdminOrganizationResponse> toResponseList(final List<Organization> organizations) {
        return organizations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AdminOrganizationResponse toResponse(final Organization organization) {
        return AdminOrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .type(organization.getOrganizationType())
                .build();
    }
}
