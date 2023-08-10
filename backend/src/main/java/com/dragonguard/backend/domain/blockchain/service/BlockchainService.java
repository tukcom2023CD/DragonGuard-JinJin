package com.dragonguard.backend.domain.blockchain.service;

import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.domain.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
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
    @Value("#{'${admin}'.split(',')}")
    private List<String> admins;

    public void setTransaction(final Member member, final long contribution, final ContributeType contributeType) {
        if (contribution <= 0) return;

        Blockchain blockchain = findBlockchainOfType(member, contributeType);
        if (!blockchain.isNewHistory(contribution)) return;

        sendSmartContract(member, contribution - blockchain.getSumOfAmount(), blockchain);
    }

    public void sendSmartContract(final Member member, final long contribution, final Blockchain blockchain) {
        String walletAddress = member.getWalletAddress();
        String transactionHash = transferTransaction(contribution, blockchain.getContributeType(), walletAddress);
        BigInteger amount = balanceOfTransaction(walletAddress);

        if (hasSameAmount(contribution, amount)) {
            blockchain.addHistory(amount, transactionHash);
            return; // 일반 유저는 모두 여기서 return 됩니다.
        }

        // 블록체인 스마트 컨트랙트를 배포한 사람의 경우 맨 처음 토큰 개수가 다르게 부여되는 현상 때문에 작성한 코드
        if (admins.stream().anyMatch(admin -> admin.strip().equals(member.getGithubId()))) {
            blockchain.addHistory(BigInteger.valueOf(contribution), transactionHash);
        }
    }

    private BigInteger balanceOfTransaction(final String walletAddress) {
        return smartContractService.balanceOf(walletAddress);
    }

    private String transferTransaction(final long contribution, final ContributeType contributeType, final String walletAddress) {
        return smartContractService.transfer(contribution, contributeType, walletAddress);
    }

    @Transactional(readOnly = true)
    public List<BlockchainResponse> getBlockchainList() {
        UUID memberId = authService.getLoginUserId();
        return getBlockchainResponses(memberId);
    }

    private List<BlockchainResponse> getBlockchainResponses(final UUID memberId) {
        return blockchainMapper.toBlockchainResponseList(blockchainRepository.findByMemberId(memberId));
    }

    @Override
    public Blockchain loadEntity(final Long id) {
        return blockchainRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean hasSameAmount(final long contribution, final BigInteger amount) {
        return contribution == amount.longValue();
    }

    public Blockchain findBlockchainOfType(final Member member, final ContributeType contributeType) {
        if(blockchainRepository.existsByMemberAndContributeType(member, contributeType)) {
            return blockchainRepository.findByMemberAndContributeType(member, contributeType)
                    .orElseThrow(EntityNotFoundException::new);
        }
        return blockchainRepository.save(blockchainMapper.toEntity(member, contributeType));
    }
}
