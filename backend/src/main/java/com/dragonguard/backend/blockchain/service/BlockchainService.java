package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockchainService {
    private final BlockchainRepository blockchainRepository;
    private final TransactionService transactionService;
    private final BlockchainMapper blockchainMapper;
    private final MemberRepository memberRepository;

    public void setTransaction(ContractRequest request) {
        Member member = memberRepository.findMemberByGithubId(request.getGithubId())
                .orElseThrow(EntityNotFoundException::new);

        if(blockchainRepository.existsByMemberId(member.getId())) {
            Long sum = blockchainRepository.findByMemberId(member.getId()).stream()
                    .mapToLong(i -> Long.valueOf(String.valueOf(i.getAmount()))).sum();
            Long num = Long.valueOf(String.valueOf(request.getAmount())) - sum;
            if (num > 0) {
                request.setAmount(BigInteger.valueOf(num));
            } else {
                return;
            }
        }

        transactionService.transfer(request);
        BigInteger amount = transactionService.balanceOf(request.getAddress());

        blockchainRepository.save(blockchainMapper.toEntity(amount, member, request));
    }

    public List<BlockchainResponse> getBlockchainList(Long memberId) {
        return blockchainRepository.findByMemberId(memberId).stream()
                .map(blockchainMapper::toResponse)
                .collect(Collectors.toList());
    }
}
