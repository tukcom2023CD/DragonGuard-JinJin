package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.domain.email.service.EmailServiceImpl;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.global.dto.IdResponse;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 김승진
 * @description 파사드 패턴으로 뽑아낸 조직 파사드 서비스
 */

@TransactionService
@RequiredArgsConstructor
public class OrganizationEmailFacade implements OrganizationService, EmailService {
    private final OrganizationServiceImpl organizationServiceImpl;
    private final EmailServiceImpl emailServiceImpl;
    private final AuthService authService;

    public IdResponse<Long> addMemberAndSendEmail(AddMemberRequest addMemberRequest) {
        organizationServiceImpl.findAndAddMember(addMemberRequest);
        return emailServiceImpl.sendAndSaveEmail();
    }

    public CheckCodeResponse isCodeMatching(final EmailRequest emailRequest) {
        Long id = emailRequest.getId();

        if (isValidCode(emailRequest)) return new CheckCodeResponse(false);
        deleteCode(id);

        Member member = authService.getLoginUser();
        member.finishAuth(organizationServiceImpl.loadEntity(emailRequest.getOrganizationId()));
        return new CheckCodeResponse(true);
    }

    private boolean isValidCode(EmailRequest emailRequest) {
        return !emailServiceImpl.loadEntity(emailRequest.getId()).getCode().equals(emailRequest.getCode());
    }

    @Override
    public IdResponse<Long> saveOrganization(OrganizationRequest organizationRequest) {
        return organizationServiceImpl.saveOrganization(organizationRequest);
    }

    @Override
    public void findAndAddMember(AddMemberRequest addMemberRequest) {
        organizationServiceImpl.findAndAddMember(addMemberRequest);
    }

    @Override
    public List<OrganizationResponse> findByType(OrganizationType organizationType, Pageable pageable) {
        return organizationServiceImpl.findByType(organizationType, pageable);
    }

    @Override
    public List<OrganizationResponse> getOrganizationRank(Pageable pageable) {
        return organizationServiceImpl.getOrganizationRank(pageable);
    }

    @Override
    public List<OrganizationResponse> getOrganizationRankByType(OrganizationType type, Pageable pageable) {
        return organizationServiceImpl.getOrganizationRankByType(type, pageable);
    }

    @Override
    public List<OrganizationResponse> searchOrganization(OrganizationType type, String name, Pageable pageable) {
        return organizationServiceImpl.searchOrganization(type, name, pageable);
    }

    @Override
    public IdResponse<Long> findByName(String name) {
        return organizationServiceImpl.findByName(name);
    }

    @Override
    public IdResponse<Long> sendAndSaveEmail() {
        return emailServiceImpl.sendAndSaveEmail();
    }

    @Override
    public void deleteCode(Long id) {
        emailServiceImpl.deleteCode(id);
    }

    @Override
    public void sendEmail(String memberEmail, int random) {
        emailServiceImpl.sendEmail(memberEmail, random);
    }
}
