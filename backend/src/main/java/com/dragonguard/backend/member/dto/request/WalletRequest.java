package com.dragonguard.backend.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author 김승진
 * @description 지갑 주소 요청 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {
    private UUID id;
    private String walletAddress;
}
