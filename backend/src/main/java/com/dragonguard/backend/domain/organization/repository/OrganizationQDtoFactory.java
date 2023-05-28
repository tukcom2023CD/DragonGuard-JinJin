package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.organization.dto.response.QOrganizationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.dragonguard.backend.domain.organization.entity.QOrganization.organization;


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
