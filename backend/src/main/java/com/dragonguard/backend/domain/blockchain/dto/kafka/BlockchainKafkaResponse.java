package com.dragonguard.backend.domain.blockchain.dto.kafka;

import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainKafkaResponse {
    private UUID memberId;
    private Long amount;
    private ContributeType contributeType;
}
