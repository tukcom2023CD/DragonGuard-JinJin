package com.dragonguard.backend.domain.blockchain.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.entity.History;
import com.dragonguard.backend.domain.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.domain.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
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
    private final MemberRepository memberRepository;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    @Value("#{'${admin}'.split(',')}")
    private List<String> admins;

    public void setTransaction(final Blockchain blockchain, final Member member, final long contribution, final ContributeType contributeType) {
        if (contribution < 0) return;

        String walletAddress = member.getWalletAddress();
        String transactionHash = transferTransaction(contribution, contributeType, walletAddress);
        BigInteger amount = transferAndGetBalanceOfTransaction(walletAddress);

        if (hasSameAmount(contribution, amount)) {
            blockchain.addHistory(amount, transactionHash);
            return;
        }

        if (admins.stream().anyMatch(admin -> admin.strip().equals(member.getGithubId()))) {
            blockchain.addHistory(amount, transactionHash);
        }
    }

    private BigInteger transferAndGetBalanceOfTransaction(final String walletAddress) {
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

    private List<BlockchainResponse> getBlockchainResponses(UUID memberId) {
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

    public List<BlockchainResponse> updateAndGetBlockchainInfo() {
        Member member = memberRepository.findById(authService.getLoginUserId()).orElseThrow(EntityNotFoundException::new);
        if (!member.isWalletAddressExists()) return List.of();

        sendSmartContractTransaction(member, 0);

        member.validateWalletAddressAndUpdateTier();
        return getBlockchainResponses(member.getId());
    }

    public void sendSmartContractTransaction(final Member member, final int contribution) {
        int commitSum = member.getCommitSumWithRelation();
        int issueSum = member.getIssueSumWithRelation();
        int pullRequestSum = member.getPullRequestSumWithRelation();
        int reviewSum = member.getCodeReviewSumWithRelation();

        if (commitSum + issueSum + pullRequestSum + reviewSum != contribution){
            applyTransactions(member, commitSum, issueSum, pullRequestSum, reviewSum);
        }
    }

    private void applyTransactions(
            final Member member,
            final int commitSum,
            final int issueSum,
            final int pullRequestSum,
            final int reviewSum) {

        Blockchain commit = getBlockchainOfType(member, ContributeType.COMMIT);
        Blockchain issue = getBlockchainOfType(member, ContributeType.ISSUE);
        Blockchain pullRequest = getBlockchainOfType(member, ContributeType.PULL_REQUEST);
        Blockchain codeReview = getBlockchainOfType(member, ContributeType.CODE_REVIEW);

        long newCommit = getNewContribution(commitSum, commit.getHistories());
        long newIssue = getNewContribution(issueSum, issue.getHistories());
        long newPullRequest = getNewContribution(pullRequestSum, pullRequest.getHistories());
        long newCodeReview = getNewContribution(reviewSum, codeReview.getHistories());

        if (commit.isNewHistory(newCommit)) sendRequestToKafka(member.getId(), newCommit, ContributeType.COMMIT, commit.getId());

        if (issue.isNewHistory(newIssue)) sendRequestToKafka(member.getId(), newIssue, ContributeType.ISSUE, issue.getId());

        if (pullRequest.isNewHistory(newPullRequest)) sendRequestToKafka(member.getId(), newPullRequest, ContributeType.PULL_REQUEST, pullRequest.getId());

        if (codeReview.isNewHistory(newCodeReview)) sendRequestToKafka(member.getId(), newCodeReview, ContributeType.CODE_REVIEW, codeReview.getId());
    }

    private long getNewContribution(final int contribution, final List<History> histories) {
        return contribution - histories.stream().map(History::getAmount).mapToLong(BigInteger::longValue).sum();
    }

    private Blockchain getBlockchainOfType(Member member, ContributeType contributeType) {
        if (blockchainRepository.existsByMemberAndContributeType(member, contributeType)) {
            return blockchainRepository.findByMemberAndContributeType(member, contributeType)
                    .orElseThrow(EntityNotFoundException::new);
        }
        return blockchainRepository.save(blockchainMapper.toEntity(member, contributeType));
    }

    private void sendRequestToKafka(UUID memberId, Long amount, ContributeType contributeType, Long blockchainId) {
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(memberId, amount, contributeType, blockchainId));
    }
}
