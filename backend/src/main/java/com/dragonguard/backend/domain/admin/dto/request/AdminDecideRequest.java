package com.dragonguard.backend.domain.admin.dto.request;

import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 관리자가 조직 승인 요청을 승인/반려할 때 보내는 데이터 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDecideRequest {
    @NotNull
    private Long id;
    @NotNull
    private OrganizationStatus decide;
}
