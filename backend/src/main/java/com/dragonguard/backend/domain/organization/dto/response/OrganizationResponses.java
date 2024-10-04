package com.dragonguard.backend.domain.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponses {
    private List<OrganizationResponse> data;
}
