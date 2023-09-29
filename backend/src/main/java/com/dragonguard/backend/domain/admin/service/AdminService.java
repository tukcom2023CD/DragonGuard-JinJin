package com.dragonguard.backend.domain.admin.service;

import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.admin.mapper.AdminMapper;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 김승진
 * @description 관리자 기능을 수행하는 Service
 */

@TransactionService
@RequiredArgsConstructor
public class AdminService {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private final OrganizationRepository organizationRepository;
    private final AdminMapper adminMapper;

    public List<AdminOrganizationResponse> decideRequestedOrganization(final AdminDecideRequest adminDecideRequest) {
        final Organization organization = getOrganization(adminDecideRequest);
        final OrganizationStatus beforeStatus = organization.getOrganizationStatus();
        organization.updateStatus(adminDecideRequest.getDecide());

        final List<Organization> organizations = organizationRepository
                .findAllByOrganizationStatus(beforeStatus, PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE));

        return adminMapper.toResponseList(organizations);
    }

    private Organization getOrganization(final AdminDecideRequest adminDecideRequest) {
        return organizationRepository.findById(adminDecideRequest.getId())
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<AdminOrganizationResponse> findOrganizationsByStatus(final OrganizationStatus status, final Pageable pageable) {
        List<Organization> organizations = organizationRepository
                .findAllByOrganizationStatus(status, pageable);

        return adminMapper.toResponseList(organizations);
    }
}
