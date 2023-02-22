package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.blockchain.dto.response.ContractResponse;
import com.dragonguard.backend.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockchainService {
    private final BlockchainRepository blockchainRepository;
    private final TransactionService transactionService;
    private final BlockchainMapper blockchainMapper;
    private final MemberService memberService;

    public void setTransaction(ContractRequest request) {
        transactionService.set(request);
        ContractResponse info = transactionService.getInfo(request.getAddress());
        Member member = memberService.findMemberByGithubId(request.getName());
        blockchainRepository.save(blockchainMapper.toEntity(info, member, request.getAddress()));
    }

    public List<BlockchainResponse> getBlockchainList(Long memberId) {
        return blockchainRepository.findByMemberId(memberId).stream()
                .map(blockchainMapper::toResponse)
                .collect(Collectors.toList());
    }
}
