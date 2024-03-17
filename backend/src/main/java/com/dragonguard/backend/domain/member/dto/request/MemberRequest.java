package com.dragonguard.backend.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 김승진
 * @description 멤버 요청 정보를 담는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @NotBlank private String githubId;
}
