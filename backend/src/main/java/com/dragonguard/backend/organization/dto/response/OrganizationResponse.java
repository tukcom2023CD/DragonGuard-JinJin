package com.dragonguard.backend.organization.dto.response;

import com.dragonguard.backend.organization.entity.OrganizationType;
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
@AllArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String name;
    private OrganizationType organizationType;
    private String email;
}
