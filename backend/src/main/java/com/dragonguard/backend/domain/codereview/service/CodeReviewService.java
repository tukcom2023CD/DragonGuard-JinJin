package com.dragonguard.backend.domain.codereview.service;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.codereview.mapper.CodeReviewMapper;
import com.dragonguard.backend.domain.codereview.repository.CodeReviewRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
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

    public void saveCodeReviews(final Member member, final Integer codeReviewNum, final Integer year) {
        if (codeReviewRepository.existsByMemberAndYear(member, year)) {
            findCodeReviewAndUpdateNum(member, codeReviewNum, year);
            return;
        }
        codeReviewRepository.save(codeReviewMapper.toEntity(codeReviewNum, year, member));
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
