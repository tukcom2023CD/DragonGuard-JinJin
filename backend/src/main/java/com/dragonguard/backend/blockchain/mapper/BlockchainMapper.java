package com.dragonguard.backend.blockchain.mapper;

import com.dragonguard.backend.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.blockchain.dto.response.ContractResponse;
import com.dragonguard.backend.blockchain.entity.Blockchain;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BlockchainMapper {

    public Blockchain toEntity(ContractResponse response, Member member, String address) {
        return Blockchain.builder()
                .contributeType(ContributeType.valueOf(response.getContributeType().toUpperCase()))
                .amount(response.getAmount())
                .address(address)
                .member(member)
                .build();
    }

    public BlockchainResponse toResponse(Blockchain blockchain) {
        return BlockchainResponse.builder()
                .id(blockchain.getId())
                .contributeType(blockchain.getContributeType())
                .amount(blockchain.getAmount())
                .name(blockchain.getMember().getName())
                .memberId(blockchain.getMember().getId())
                .build();
    }
}
