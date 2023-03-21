package com.dragonguard.backend.organization.dto.response;

import com.dragonguard.backend.organization.entity.OrganizationType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
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
    private String email;

    @QueryProjection
    public OrganizationResponse(Long id, String name, OrganizationType organizationType, String email) {
        this.id = id;
        this.name = name;
        this.organizationType = organizationType;
        this.email = email;
    }
}
