package com.dragonguard.backend.domain.admin.dto.request;

import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DecideRequest {
    private Long id;
    private OrganizationStatus decide;
}
