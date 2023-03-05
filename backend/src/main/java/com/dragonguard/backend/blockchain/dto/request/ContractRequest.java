package com.dragonguard.backend.blockchain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @author 김승진
 * @description 블록체인 관련 요청 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String address;
    private String contributeType;
    @Setter
    private BigInteger amount;
    private String githubId;
}
