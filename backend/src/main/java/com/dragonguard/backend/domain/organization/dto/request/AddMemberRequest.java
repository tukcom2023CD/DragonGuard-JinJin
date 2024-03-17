package com.dragonguard.backend.domain.organization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 조직에 멤버의 추가를 위한 요청 정보를 담는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequest {
    @NotNull private Long organizationId;
    @Email private String email;
}
