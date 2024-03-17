package com.dragonguard.backend.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 김승진
 * @description 지갑 주소 요청 정보를 담는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {
    @NotBlank private String walletAddress;
}
