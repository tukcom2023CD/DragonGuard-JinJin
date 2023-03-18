package com.dragonguard.backend.blockchain.dto.response;

import com.dragonguard.backend.blockchain.entity.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author 김승진
 * @description 블록체인 관련 응답 정보를 담는 dto
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainResponse {
    private Long id;
    private ContributeType contributeType;
    private BigInteger amount;
    private String githubId;
    private UUID memberId;
}
