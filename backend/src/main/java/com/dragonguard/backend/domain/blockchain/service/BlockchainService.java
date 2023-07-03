package com.dragonguard.backend.domain.blockchain.service;

import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.mapper.BlockchainMapper;
import com.dragonguard.backend.domain.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    @Value("#{'${admin}'.split(',')}")
    private List<String> admins;

    public void setTransaction(final Member member, final long contribution, final ContributeType contributeType) {
        if (contribution < 0) return;

        String walletAddress = member.getWalletAddress();
        String transactionHash = transferTransaction(contribution, contributeType, walletAddress);
        BigInteger amount = transferAndGetBalanceOfTransaction(walletAddress);

        if (hasSameAmount(contribution, amount)) {
            blockchainRepository.save(blockchainMapper.toEntity(amount, member, contributeType, transactionHash));
            return;
        }

        if (admins.stream().anyMatch(admin -> admin.strip().equals(member.getGithubId()))) {
            blockchainRepository.save(blockchainMapper.toEntity(BigInteger.valueOf(contribution), member, contributeType, transactionHash));
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
        return blockchainMapper.toResponseList(blockchainRepository.findAllByMemberId(memberId));
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

        sendSmartContractTransaction(member, false, 0);

        member.validateWalletAddressAndUpdateTier();
        return getBlockchainResponses(member.getId());
    }

    public void sendSmartContractTransaction(final Member member, final boolean flag, final int contribution) {
        int commitSum = member.getCommitSumWithRelation();
        int issueSum = member.getIssueSumWithRelation();
        int pullRequestSum = member.getPullRequestSumWithRelation();
        int reviewSum = member.getSumOfReviews().orElse(0);

        if (commitSum + issueSum + pullRequestSum + reviewSum == contribution) return;

        applyTransactions(member, commitSum, issueSum, pullRequestSum, reviewSum, flag, contribution);
    }

    private void applyTransactions(
            final Member member,
            final int commitSum,
            final int issueSum,
            final int pullRequestSum,
            final int reviewSum,
            final boolean flag,
            final int contribution) {
        List<Blockchain> commit = blockchainRepository.findAllByMemberAndContributeType(member, ContributeType.COMMIT);
        List<Blockchain> issue = blockchainRepository.findAllByMemberAndContributeType(member, ContributeType.ISSUE);
        List<Blockchain> pullRequest = blockchainRepository.findAllByMemberAndContributeType(member, ContributeType.PULL_REQUEST);
        List<Blockchain> review = blockchainRepository.findAllByMemberAndContributeType(member, ContributeType.CODE_REVIEW);

        List<Blockchain> blockchains = blockchainRepository.findAllByMemberId(member.getId());
        int savedSum = blockchains.stream()
                .map(Blockchain::getAmount)
                .mapToInt(BigInteger::intValue)
                .sum();

        long newCommit = getNewContribution(commitSum, commit);
        long newIssue = getNewContribution(issueSum, issue);
        long newPullRequest = getNewContribution(pullRequestSum, pullRequest);
        long newCodeReview = getNewContribution(reviewSum, review);

        if (newCommit > 0) setTransaction(member, newCommit, ContributeType.COMMIT);
        if (newCommit + savedSum == contribution) return;

        if (newIssue > 0) setTransaction(member, newIssue, ContributeType.ISSUE);
        if (newCommit + newIssue + savedSum == contribution) return;

        if (newPullRequest > 0) setTransaction(member, newPullRequest, ContributeType.PULL_REQUEST);
        if (newCommit + newIssue + newPullRequest + savedSum == contribution) return;

        if (blockchains.size() == member.getContributionSize() || newCommit + newIssue + newPullRequest + savedSum == member.getContribution()) return;
        if ((flag && newCodeReview > 0) || (review.isEmpty() && reviewSum > 0)) setTransaction(member, newCodeReview, ContributeType.CODE_REVIEW);
    }

    private long getNewContribution(final int contribution, final List<Blockchain> blockchains) {
        return contribution - blockchains.stream().map(Blockchain::getAmount).mapToLong(BigInteger::longValue).sum();
    }
}
