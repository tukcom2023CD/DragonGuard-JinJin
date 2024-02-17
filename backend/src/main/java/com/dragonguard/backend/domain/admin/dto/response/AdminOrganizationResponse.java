package com.dragonguard.backend.domain.admin.dto.response;

import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author 김승진
 * @description 관리자가 조직 목록을 볼 때 필요한 dto
 */
@Getter
@Builder
@AllArgsConstructor
public class AdminOrganizationResponse {
    private Long id;
    private String name;
    private OrganizationType type;
    private String emailEndpoint;
}
