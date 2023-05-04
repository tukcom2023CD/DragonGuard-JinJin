package com.dragonguard.backend.domain.admin.dto.response;

import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminOrganizationResponse {
    private Long id;
    private String name;
    private OrganizationType type;
}
