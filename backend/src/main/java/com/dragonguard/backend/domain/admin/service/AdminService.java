package com.dragonguard.backend.domain.admin.service;

import com.dragonguard.backend.domain.admin.dto.request.DecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.OrganizationAdminResponse;
import com.dragonguard.backend.domain.admin.mapper.AdminMapper;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final OrganizationRepository organizationRepository;
    private final AdminMapper adminMapper;

    @Transactional
    public List<OrganizationAdminResponse> decideRequestedOrganization(DecideRequest decideRequest) {
        Organization organization = organizationRepository.findById(decideRequest.getId())
                .orElseThrow(EntityNotFoundException::new);
        OrganizationStatus beforeStatus = organization.getOrganizationStatus();
        organization.updateStatus(decideRequest.getDecide());

        List<Organization> organizations = organizationRepository
                .findAllByOrganizationStatus(beforeStatus, PageRequest.of(0, 20));

        return adminMapper.toResponseList(organizations);
    }

    public List<OrganizationAdminResponse> getOrganizationsByStatus(OrganizationStatus status, Pageable pageable) {
        List<Organization> organizations = organizationRepository
                .findAllByOrganizationStatus(status, pageable);

        return adminMapper.toResponseList(organizations);
    }
}
