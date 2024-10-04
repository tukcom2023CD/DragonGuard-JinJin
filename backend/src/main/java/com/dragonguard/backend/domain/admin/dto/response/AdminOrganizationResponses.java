package com.dragonguard.backend.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrganizationResponses {
    private List<AdminOrganizationResponse> data;
}
