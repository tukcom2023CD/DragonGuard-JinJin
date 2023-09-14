package com.dragonguard.backend.domain.codereview.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.codereview.repository.CodeReviewRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.mapper.ContributionEntityMapper;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 코드리뷰 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class CodeReviewService implements ContributionService<CodeReview, Long> {
    private static final long NO_AMOUNT = 0L;
    private final CodeReviewRepository codeReviewRepository;
    private final ContributionEntityMapper<CodeReview> codeReviewMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer codeReviewNum, final Integer year) {
        final Blockchain blockchain = blockchainService.findBlockchainOfType(member, ContributeType.CODE_REVIEW);

        if (existsByMemberAndYear(member, year)) {
            updateAndSendTransaction(member, codeReviewNum, year, blockchain);
            return;
        }
        codeReviewRepository.save(codeReviewMapper.toEntity(member, codeReviewNum, year));
        sendTransaction(member, codeReviewNum.longValue(), blockchain);
    }

    private void updateAndSendTransaction(final Member member, final Integer codeReviewNum, final Integer year, final Blockchain blockchain) {
        final CodeReview codeReview = getCodeReview(member, year);
        final long newBlockchainAmount = codeReviewNum - blockchain.getSumOfAmount();
        if (isNotUpdatable(codeReviewNum, codeReview, newBlockchainAmount)) {
            return;
        }
        codeReview.updateCodeReviewNum(codeReviewNum);
        sendTransaction(member, codeReviewNum.longValue(), blockchain);
    }

    private boolean isNotUpdatable(final Integer codeReviewNum, final CodeReview codeReview, final long newBlockchainAmount) {
        return codeReview.isNotUpdatable(codeReviewNum) && newBlockchainAmount == NO_AMOUNT;
    }

    private CodeReview getCodeReview(final Member member, final Integer year) {
        return codeReviewRepository.findByMemberAndYear(member, year)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean existsByMemberAndYear(final Member member, final Integer year) {
        return codeReviewRepository.existsByMemberAndYear(member, year);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (isInvalidToTransaction(member, amount, blockchain)) {
            return;
        }
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.CODE_REVIEW));
    }

    private boolean isInvalidToTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        return amount <= NO_AMOUNT || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount);
    }

    @Override
    public CodeReview loadEntity(Long id) {
        return codeReviewRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
