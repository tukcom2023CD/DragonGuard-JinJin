package com.dragonguard.backend.blockchain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String address;
    private String contributeType;
    private BigInteger amount;
    private String name;
}
