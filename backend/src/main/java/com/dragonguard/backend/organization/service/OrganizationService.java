package com.dragonguard.backend.organization.service;

import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.organization.entity.Organization;
import com.dragonguard.backend.organization.entity.OrganizationType;
import com.dragonguard.backend.organization.mapper.OrganizationMapper;
import com.dragonguard.backend.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) 관련 서비스 로직을 수행하는 클래스
 */

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final AuthService authService;

    public IdResponse<Long> saveOrganization(OrganizationRequest organizationRequest) {
        Organization organization = organizationRepository.save(organizationMapper.toEntity(organizationRequest));
        addMember(organization);
        return new IdResponse<>(organization.getId());
    }

    public void findAndAddMember(AddMemberRequest addMemberRequest) {
        Organization organization = getEntity(addMemberRequest.getOrganizationId());
        addMember(organization);
    }

    public List<OrganizationType> getTypes() {
        return Arrays.asList(OrganizationType.values());
    }

    public List<OrganizationResponse> findByType(OrganizationType organizationType) {
        return organizationRepository.findByType(organizationType).stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMember(Organization organization) {
        organization.addMember(authService.getLoginUser());
    }

    private Organization getEntity(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<OrganizationResponse> getOrganizationRank(Pageable pageable) {
        return organizationRepository.findRank(pageable);
    }

    public List<OrganizationResponse> getOrganizationRankByType(OrganizationType type, Pageable pageable) {
        return organizationRepository.findRankByType(type, pageable);
    }
}
