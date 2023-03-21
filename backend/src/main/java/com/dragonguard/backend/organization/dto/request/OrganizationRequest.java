package com.dragonguard.backend.organization.dto.request;

import com.dragonguard.backend.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 조직(회사, 대학교)관련 요청정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequest {
    @NotBlank
    private String name;
    @NotNull
    private OrganizationType organizationType;
    @NotBlank
    private String emailEndpoint;
}
