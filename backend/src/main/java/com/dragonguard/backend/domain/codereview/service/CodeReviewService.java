package com.dragonguard.backend.domain.codereview.service;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.codereview.mapper.CodeReviewMapper;
import com.dragonguard.backend.domain.codereview.repository.CodeReviewRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 코드리뷰 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class CodeReviewService implements EntityLoader<CodeReview, Long> {
    private final CodeReviewRepository codeReviewRepository;
    private final CodeReviewMapper codeReviewMapper;
    private final KafkaProducer<BlockchainKafkaRequest> blockchainKafkaProducer;
    private final BlockchainService blockchainService;

    public void saveCodeReviews(final Member member, final Integer codeReviewNum, final Integer year) {
        if (codeReviewRepository.existsByMemberAndYear(member, year)) {
            Integer newAmount = findCodeReviewAndUpdateNum(member, codeReviewNum, year);
            sendTransaction(member, newAmount.longValue());
            return;
        }
        codeReviewRepository.save(codeReviewMapper.toEntity(codeReviewNum, year, member));
        sendTransaction(member, codeReviewNum.longValue());
    }

    private void sendTransaction(Member member, Long amount) {
        if (amount <= 0 || !member.isWalletAddressExists()) return;
        Long blockchainId = blockchainService.getBlockchainOfType(member, ContributeType.CODE_REVIEW).getId();
        blockchainKafkaProducer.send(new BlockchainKafkaRequest(member.getId(), amount, ContributeType.CODE_REVIEW, blockchainId));
    }

    private Integer findCodeReviewAndUpdateNum(final Member member, final Integer codeReviewNum, final Integer year) {
        CodeReview codeReview = codeReviewRepository.findByMemberAndYear(member, year).orElseThrow(EntityNotFoundException::new);
        int newAmount = codeReviewNum - codeReview.getAmount();
        codeReview.updateCodeReviewNum(codeReviewNum);
        return newAmount;
    }

    @Override
    public CodeReview loadEntity(Long id) {
        return codeReviewRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
