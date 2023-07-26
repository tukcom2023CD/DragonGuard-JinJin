package com.dragonguard.backend.domain.blockchain.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmartContractKafkaRequest {
    private UUID memberId;
    private Long amount;
    private Long blockchainId;
}
