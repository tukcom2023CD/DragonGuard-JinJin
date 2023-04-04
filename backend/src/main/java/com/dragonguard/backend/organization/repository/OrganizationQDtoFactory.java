package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.member.entity.QMember;
import com.dragonguard.backend.organization.dto.response.QOrganizationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.dragonguard.backend.member.entity.QMember.member;
import static com.dragonguard.backend.organization.entity.QOrganization.organization;


/**
 * @author 김승진
 * @description Organization 프로젝션 적용을 위한 클래스
 */

@Component
public class OrganizationQDtoFactory {

    @Bean
    public QOrganizationResponse qOrganizationResponse() {
        return new QOrganizationResponse(
                organization.id,
                organization.name,
                organization.organizationType,
                organization.emailEndpoint,
                organization.sumOfMemberTokens);
    }
}
