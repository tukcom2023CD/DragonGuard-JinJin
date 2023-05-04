package com.dragonguard.backend.domain.admin.mapper;

import com.dragonguard.backend.domain.admin.dto.response.OrganizationAdminResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminMapper {

    public List<OrganizationAdminResponse> toResponseList(List<Organization> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrganizationAdminResponse toResponse(Organization entity) {
        return OrganizationAdminResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
