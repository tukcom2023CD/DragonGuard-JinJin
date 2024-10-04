package com.dragonguard.backend.domain.blockchain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainResponses {
    private List<BlockchainResponse> data;
}
