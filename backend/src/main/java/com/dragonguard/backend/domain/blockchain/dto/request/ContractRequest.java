package com.dragonguard.backend.domain.blockchain.dto.request;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 김승진
 * @description 블록체인 관련 요청 정보를 담는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String contributeType;
    @Setter private BigInteger amount;
}
