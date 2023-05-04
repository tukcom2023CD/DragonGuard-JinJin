package com.dragonguard.backend.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrganizationAdminResponse {
    private Long id;
    private String name;
}
