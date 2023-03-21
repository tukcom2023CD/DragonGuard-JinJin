package com.dragonguard.backend.organization.dto.request;

import com.dragonguard.backend.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 조직(회사, 대학교)관련 요청정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequest {
    private String name;
    private OrganizationType organizationType;
    private String email;
}
