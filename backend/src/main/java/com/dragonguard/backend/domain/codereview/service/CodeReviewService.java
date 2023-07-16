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
    private final CodeReviewRepository codeReviewRepository;
    private final ContributionEntityMapper<CodeReview> codeReviewMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    @Override
    public void saveContribution(final Member member, final Integer codeReviewNum, final Integer year) {
        Blockchain blockchain = blockchainService.getBlockchainOfType(member, ContributeType.CODE_REVIEW);

        if (codeReviewRepository.existsByMemberAndYear(member, year)) {
            findCodeReviewAndUpdateNum(member, codeReviewNum, year);
            sendTransaction(member, codeReviewNum - blockchain.getSumOfAmount(), blockchain);
            return;
        }
        codeReviewRepository.save(codeReviewMapper.toEntity(member, codeReviewNum, year));
        sendTransaction(member, codeReviewNum.longValue(), blockchain);
    }

    private void sendTransaction(final Member member, final Long amount, final Blockchain blockchain) {
        if (amount <= 0 || !member.isWalletAddressExists() || !blockchain.isNewHistory(amount)) return;
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.CODE_REVIEW));
    }

    private void findCodeReviewAndUpdateNum(final Member member, final Integer codeReviewNum, final Integer year) {
        CodeReview codeReview = codeReviewRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        codeReview.updateCodeReviewNum(codeReviewNum);
    }

    @Override
    public CodeReview loadEntity(Long id) {
        return codeReviewRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
