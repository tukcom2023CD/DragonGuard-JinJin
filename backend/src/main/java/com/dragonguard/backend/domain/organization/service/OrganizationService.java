package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.dto.response.RelatedRankWithMemberResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.global.dto.IdResponse;
import com.dragonguard.backend.global.template.service.EntityLoader;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrganizationService extends EntityLoader<Organization, Long> {
    IdResponse<Long> saveOrganization(final OrganizationRequest organizationRequest);

    void findAndAddMember(final AddMemberRequest addMemberRequest);

    List<OrganizationResponse> findByType(
            final OrganizationType organizationType, final Pageable pageable);

    List<OrganizationResponse> findOrganizationRank(final Pageable pageable);

    List<OrganizationResponse> findOrganizationRankByType(
            final OrganizationType type, final Pageable pageable);

    List<OrganizationResponse> searchOrganization(
            final OrganizationType type, final String name, final Pageable pageable);

    IdResponse<Long> getByName(final String name);

    RelatedRankWithMemberResponse findRankingByMemberId(UUID id, String githubId);
}
