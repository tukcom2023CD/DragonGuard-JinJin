package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.global.dto.IdResponse;
import com.dragonguard.backend.global.template.service.EntityLoader;
import com.dragonguard.backend.global.template.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 김승진
 * @description 파사드 패턴으로 뽑아낸 조직 파사드 서비스
 */

@TransactionService
@RequiredArgsConstructor
public class OrganizationEmailFacade {
    private final EntityLoader<Email, Long> emailEntityLoader;
    private final EntityLoader<Organization, Long> organizationEntityLoader;
    private final OrganizationService organizationService;
    private final EmailService emailService;
    private final AuthService authService;

    public IdResponse<Long> addMemberAndSendEmail(final AddMemberRequest addMemberRequest) {
        organizationService.findAndAddMember(addMemberRequest);
        return emailService.sendAndSaveEmail();
    }

    public CheckCodeResponse isCodeMatching(final EmailRequest emailRequest) {
        final Long id = emailRequest.getId();

        if (isValidCode(emailRequest)) {
            return new CheckCodeResponse(false);
        }
        deleteCode(id);

        Member member = authService.getLoginUser();
        member.finishAuth(organizationEntityLoader.loadEntity(emailRequest.getOrganizationId()));
        return new CheckCodeResponse(true);
    }

    private boolean isValidCode(final EmailRequest emailRequest) {
        final Email email = emailEntityLoader.loadEntity(emailRequest.getId());
        return email.notMatchCode(emailRequest.getCode());
    }

    public IdResponse<Long> saveOrganization(final OrganizationRequest organizationRequest) {
        return organizationService.saveOrganization(organizationRequest);
    }

    public List<OrganizationResponse> findByType(final OrganizationType organizationType, final Pageable pageable) {
        return organizationService.findByType(organizationType, pageable);
    }

    public List<OrganizationResponse> getOrganizationRank(final Pageable pageable) {
        return organizationService.findOrganizationRank(pageable);
    }

    public List<OrganizationResponse> getOrganizationRankByType(final OrganizationType type, final Pageable pageable) {
        return organizationService.findOrganizationRankByType(type, pageable);
    }

    public List<OrganizationResponse> searchOrganization(final OrganizationType type, final String name, final Pageable pageable) {
        return organizationService.searchOrganization(type, name, pageable);
    }

    public IdResponse<Long> getByName(final String name) {
        return organizationService.getByName(name);
    }

    public IdResponse<Long> sendAndSaveEmail() {
        return emailService.sendAndSaveEmail();
    }

    public void deleteCode(final Long id) {
        emailService.deleteCode(id);
    }
}
