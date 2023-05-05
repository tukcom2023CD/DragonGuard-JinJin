package com.dragonguard.backend.domain.admin.service;

import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
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

/**
 * @author 김승진
 * @description 관리자 기능을 수행하는 Service
 */

@Service
@RequiredArgsConstructor
public class AdminService {
    private final OrganizationRepository organizationRepository;
    private final AdminMapper adminMapper;

    @Transactional
    public List<AdminOrganizationResponse> decideRequestedOrganization(AdminDecideRequest adminDecideRequest) {
        Organization organization = organizationRepository.findById(adminDecideRequest.getId())
                .orElseThrow(EntityNotFoundException::new);
        OrganizationStatus beforeStatus = organization.getOrganizationStatus();
        organization.updateStatus(adminDecideRequest.getDecide());

        List<Organization> organizations = organizationRepository
                .findAllByOrganizationStatus(beforeStatus, PageRequest.of(0, 20));

        return adminMapper.toResponseList(organizations);
    }

    public List<AdminOrganizationResponse> getOrganizationsByStatus(OrganizationStatus status, Pageable pageable) {
        List<Organization> organizations = organizationRepository
                .findAllByOrganizationStatus(status, pageable);

        return adminMapper.toResponseList(organizations);
    }
}
