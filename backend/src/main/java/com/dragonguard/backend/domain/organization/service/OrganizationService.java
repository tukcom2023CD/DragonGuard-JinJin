package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.domain.organization.mapper.OrganizationMapper;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) 관련 서비스 로직을 수행하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class OrganizationService implements EntityLoader<Organization, Long> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final AuthService authService;
    private final EmailService emailService;

    public IdResponse<Long> saveOrganization(final OrganizationRequest organizationRequest) {
        Organization organization = getOrSaveOrganization(organizationRequest);
        return new IdResponse<>(organization.getId());
    }

    private Organization getOrSaveOrganization(final OrganizationRequest organizationRequest) {
        return organizationRepository.findByNameAndOrganizationTypeAndEmailEndpoint(
                        organizationRequest.getName(),
                        organizationRequest.getOrganizationType(),
                        organizationRequest.getEmailEndpoint())
                .orElseGet(() -> {
                    Organization organization = organizationRepository.save(organizationMapper.toEntity(organizationRequest));
                    return loadEntity(organization.getId());
                });
    }

    public IdResponse<Long> addMemberAndSendEmail(final AddMemberRequest addMemberRequest) {
        findAndAddMember(addMemberRequest);
        return emailService.sendAndSaveEmail();
    }

    private void findAndAddMember(final AddMemberRequest addMemberRequest) {
        Organization organization = loadEntity(addMemberRequest.getOrganizationId());
        Member member = authService.getLoginUser();
        organization.addMember(member, addMemberRequest.getEmail().strip());
        member.undoFinishingAuth();
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findByType(final OrganizationType organizationType, final Pageable pageable) {
        return organizationRepository.findAllByType(organizationType, pageable).stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getOrganizationRank(final Pageable pageable) {
        return organizationRepository.findRanking(pageable);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getOrganizationRankByType(final OrganizationType type, final Pageable pageable) {
        return organizationRepository.findRankingByType(type, pageable);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> searchOrganization(final OrganizationType type, final String name, final Pageable pageable) {
        return organizationRepository.findByTypeAndSearchWord(type, name, pageable);
    }

    @Transactional(readOnly = true)
    public IdResponse<Long> findByName(final String name) {
        Organization organization = organizationRepository.findByName(name)
                .orElseThrow(EntityNotFoundException::new);
        return new IdResponse<>(organization.getId());
    }

    @Override
    public Organization loadEntity(final Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
