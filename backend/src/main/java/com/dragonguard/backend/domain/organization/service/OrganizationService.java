package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.global.dto.IdResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationService {
    IdResponse<Long> saveOrganization(final OrganizationRequest organizationRequest);
    void findAndAddMember(final AddMemberRequest addMemberRequest);
    List<OrganizationResponse> findByType(final OrganizationType organizationType, final Pageable pageable);
    List<OrganizationResponse> findOrganizationRank(final Pageable pageable);
    List<OrganizationResponse> findOrganizationRankByType(final OrganizationType type, final Pageable pageable);
    List<OrganizationResponse> searchOrganization(final OrganizationType type, final String name, final Pageable pageable);
    IdResponse<Long> getByName(final String name);
}
