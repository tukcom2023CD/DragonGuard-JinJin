package com.dragonguard.backend.domain.blockchain.service;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.domain.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

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
    private final MemberRepository memberRepository;
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
        return request.getAmount().longValue() - getSumOfMemberBlockchainTokens(request, memberId);
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
        UUID memberId = authService.getLoginUserId();
        return getBlockchainResponses(memberId);
    }

    private List<BlockchainResponse> getBlockchainResponses(UUID memberId) {
        return blockchainMapper.toResponseList(blockchainRepository.findAllByMemberId(memberId));
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

    public List<BlockchainResponse> updateAndGetBlockchainInfo() {
        Member member = memberRepository.findById(authService.getLoginUserId()).orElseThrow(EntityNotFoundException::new);
        if (!member.isWalletAddressExists()) return List.of();

        sendSmartContractTransaction(member);

        member.validateWalletAddressAndUpdateTier();
        return getBlockchainResponses(member.getId());
    }

    private boolean checkAndTransaction(final Member member, final int contribution, final ContributeType contributeType) {
        if (contribution <= 0) return true;

        setTransaction(
                new ContractRequest(
                        contributeType.toString(),
                        BigInteger.valueOf(contribution)),
                member);

        return false;
    }

    public void sendSmartContractTransaction(final Member member) {
        if (checkAndTransaction(member, member.getCommitSumWithRelation(), ContributeType.COMMIT)) return;

        if (checkAndTransaction(member, member.getIssueSumWithRelation(), ContributeType.ISSUE)) return;

        if (checkAndTransaction(member, member.getPullRequestSumWithRelation(), ContributeType.PULL_REQUEST)) return;

        member.getSumOfReviews().ifPresent(rv -> checkAndTransaction(member, rv, ContributeType.CODE_REVIEW));
    }
}
