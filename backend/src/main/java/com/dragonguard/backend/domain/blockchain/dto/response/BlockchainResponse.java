package com.dragonguard.backend.domain.blockchain.dto.response;

import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author 김승진
 * @description 블록체인 관련 응답 정보를 담는 dto
 */

@Getter
@Builder
@AllArgsConstructor
public class BlockchainResponse {
    private Long id;
    private String contributeType;
    private Long amount;
    private String githubId;
    private UUID memberId;
    private String createdAt;
    private String transactionHashUrl;
}
