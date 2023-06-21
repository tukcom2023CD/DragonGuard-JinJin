package com.dragonguard.backend.domain.blockchain.service;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.domain.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 블록체인 관련 DB 요청 및 SmartContractService의 함수들의 호출을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class BlockchainService implements EntityLoader<Blockchain, Long> {
    private final BlockchainRepository blockchainRepository;
    private final SmartContractService smartContractService;
    private final BlockchainMapper blockchainMapper;
    private final AuthService authService;
    @Value("#{'${admin}'.split(',')}")
    private List<String> admins;

    public void setTransaction(final ContractRequest request, final Member member) {
        UUID memberId = member.getId();
        if (isMemberBlockchainSaved(memberId)) request.setAmount(BigInteger.valueOf(getNumOfNewContributionsWithoutSaved(request, memberId)));

        if (hasNoContribution(request)) return;
        String walletAddress = member.getWalletAddress();
        String transactionHash = transferTransaction(request, walletAddress);
        BigInteger amount = transferAndGetBalanceOfTransaction(walletAddress);

        if (hasSameAmount(request, amount)) {
            blockchainRepository.save(blockchainMapper.toEntity(amount, member, request, transactionHash));
            return;
        }
        checkAdminAndSaveBlockchain(transactionHash, request, member);
    }

    private long getNumOfNewContributionsWithoutSaved(final ContractRequest request, final UUID memberId) {
        return Long.parseLong(String.valueOf(request.getAmount())) - getSumOfMemberBlockchainTokens(request, memberId);
    }

    private long getSumOfMemberBlockchainTokens(final ContractRequest request, final UUID memberId) {
        return blockchainRepository.findAllByMemberId(memberId).stream()
                .filter(b -> b.getContributeType().equals(ContributeType.valueOf(request.getContributeType())))
                .mapToLong(b -> Long.parseLong(String.valueOf(b.getAmount())))
                .sum();
    }

    private boolean hasNoContribution(final ContractRequest request) {
        return request.getAmount().equals(BigInteger.ZERO);
    }

    private boolean isMemberBlockchainSaved(final UUID memberId) {
        return blockchainRepository.existsByMemberId(memberId);
    }

    private BigInteger transferAndGetBalanceOfTransaction(final String walletAddress) {
        return smartContractService.balanceOf(walletAddress);
    }

    private String transferTransaction(final ContractRequest request, String walletAddress) {
        return smartContractService.transfer(request, walletAddress);
    }

    @Transactional(readOnly = true)
    public List<BlockchainResponse> getBlockchainList() {
        return blockchainRepository.findAllByMemberId(authService.getLoginUserId()).stream()
                .map(blockchainMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Blockchain loadEntity(final Long id) {
        return blockchainRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void checkAdminAndSaveBlockchain(final String transactionHash, final ContractRequest request, final Member member) {
        if (admins.stream().anyMatch(admin -> admin.strip().equals(member.getGithubId()))) {
            blockchainRepository.save(blockchainMapper.toEntity(request.getAmount(), member, request, transactionHash));
        }
    }

    private boolean hasSameAmount(final ContractRequest request, final BigInteger amount) {
        return amount.equals(request.getAmount());
    }
}
