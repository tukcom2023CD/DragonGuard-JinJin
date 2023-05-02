package com.dragonguard.backend.organization.dto.response;

import com.dragonguard.backend.organization.entity.OrganizationType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 조직(회사, 대학교)관련 응답정보를 담는 dto
 */

@Getter
@Builder
@NoArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String name;
    private OrganizationType organizationType;
    private String emailEndpoint;
    private Long tokenSum;

    @QueryProjection
    public OrganizationResponse(Long id, String name, OrganizationType organizationType, String emailEndpoint, Long tokenSum) {
        this.id = id;
        this.name = name;
        this.organizationType = organizationType;
        this.emailEndpoint = emailEndpoint;
        this.tokenSum = tokenSum;
    }
}
