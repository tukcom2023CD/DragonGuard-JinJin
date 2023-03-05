package com.dragonguard.backend.blockchain.mapper;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.blockchain.entity.Blockchain;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * @author 김승진
 * @description 블록체인 Entity와 dto의 변환을 돕는 클래스
 */

@Component
public class BlockchainMapper {

    public Blockchain toEntity(BigInteger amount, Member member, ContractRequest request) {
        return Blockchain.builder()
                .contributeType(ContributeType.valueOf(request.getContributeType().toUpperCase()))
                .amount(amount)
                .address(request.getAddress())
                .member(member)
                .build();
    }

    public BlockchainResponse toResponse(Blockchain blockchain) {
        return BlockchainResponse.builder()
                .id(blockchain.getId())
                .contributeType(blockchain.getContributeType())
                .amount(blockchain.getAmount())
                .githubId(blockchain.getMember().getGithubId())
                .memberId(blockchain.getMember().getId())
                .build();
    }
}
