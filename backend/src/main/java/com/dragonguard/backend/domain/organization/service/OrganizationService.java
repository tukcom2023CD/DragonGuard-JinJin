package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.domain.organization.mapper.OrganizationMapper;
import com.dragonguard.backend.domain.organization.repository.OrganizationQueryRepository;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
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
    private final OrganizationQueryRepository organizationQueryRepository;
    private final OrganizationMapper organizationMapper;
    private final MemberService memberService;
    private final EmailService emailService;

    @Transactional
    public IdResponse<Long> saveOrganization(OrganizationRequest organizationRequest) {
        Organization organization = organizationRepository.findByNameAndOrganizationTypeAndEmailEndpoint(
                        organizationRequest.getName(), organizationRequest.getOrganizationType(), organizationRequest.getEmailEndpoint())
                .orElseGet(() -> organizationRepository.save(organizationMapper.toEntity(organizationRequest)));
        return new IdResponse<>(organization.getId());
    }

    @Transactional
    public IdResponse<Long> findAndAddMember(AddMemberRequest addMemberRequest) {
        Organization organization = getEntity(addMemberRequest.getOrganizationId());
        Member member = memberService.getLoginUserWithDatabase();
        organization.addMember(member, addMemberRequest.getEmail().trim());
        return emailService.sendEmail();
    }

    public List<OrganizationType> getTypes() {
        return Arrays.asList(OrganizationType.values());
    }

    public List<OrganizationResponse> findByType(OrganizationType organizationType, Pageable pageable) {
        return organizationRepository.findByType(organizationType, pageable).stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrganizationResponse> getOrganizationRank(Pageable pageable) {
        return organizationQueryRepository.findRanking(pageable);
    }

    public List<OrganizationResponse> getOrganizationRankByType(OrganizationType type, Pageable pageable) {
        return organizationQueryRepository.findRankingByType(type, pageable);
    }

    public List<OrganizationResponse> searchOrganization(OrganizationType type, String name, Pageable pageable) {
        return organizationQueryRepository.findByTypeAndSearchWord(type, name, pageable);
    }

    public IdResponse<Long> findByName(String name) {
        Organization organization = organizationRepository.findByName(name)
                .orElseThrow(EntityNotFoundException::new);
        return new IdResponse<>(organization.getId());
    }

    private Organization getEntity(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
