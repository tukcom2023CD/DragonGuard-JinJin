package com.dragonguard.backend.blockchain.dto.response;

import com.dragonguard.backend.blockchain.entity.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainResponse {
    private Long id;
    private ContributeType contributeType;
    private BigInteger amount;
    private String githubId;
    private Long memberId;
}
