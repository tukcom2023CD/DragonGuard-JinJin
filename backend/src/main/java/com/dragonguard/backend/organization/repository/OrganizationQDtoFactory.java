package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.organization.dto.response.QOrganizationResponse;
import com.dragonguard.backend.organization.entity.QOrganization;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
                organization.email);
    }
}
