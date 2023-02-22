package com.dragonguard.backend.blockchain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {
    private String ContributeType;
    private BigInteger amount;
    private String name;
}
