package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.blockchain.entity.Blockchain;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 블록체인 관련 DB 요청 및 TrasactionService의 함수들의 호출을 담당하는 클래스
 */

@Service
@RequiredArgsConstructor
public class BlockchainService {
    private final BlockchainRepository blockchainRepository;
    private final TransactionService transactionService;
    private final BlockchainMapper blockchainMapper;
    private final AuthService authService;

    public void setTransaction(ContractRequest request, Member member) {
        UUID memberId = member.getId();
        if (blockchainRepository.existsByMemberId(memberId)) {
            List<Blockchain> blockchains = blockchainRepository.findByMemberId(memberId);
            long sum = blockchains.stream()
                    .filter(b -> b.getContributeType().equals(ContributeType.valueOf(request.getContributeType())))
                    .mapToLong(b -> Long.parseLong(String.valueOf(b.getAmount())))
                    .sum();

            Long num = Long.parseLong(String.valueOf(request.getAmount())) - sum;
            if (num <= 0) {
                return;
            }
            request.setAmount(BigInteger.valueOf(num));
        }

        transactionService.transfer(request);
        BigInteger amount = transactionService.balanceOf(request.getAddress());

        blockchainRepository.save(blockchainMapper.toEntity(amount, member, request));
    }

    public List<BlockchainResponse> getBlockchainList() {
        return blockchainRepository.findByMemberId(authService.getLoginUser().getId()).stream()
                .map(blockchainMapper::toResponse)
                .collect(Collectors.toList());
    }
}
