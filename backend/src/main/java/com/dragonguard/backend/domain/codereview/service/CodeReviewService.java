package com.dragonguard.backend.domain.codereview.service;

import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import com.dragonguard.backend.global.template.repository.ContributionRepository;
import com.dragonguard.backend.global.template.service.ContributionService;

/**
 * @author 김승진
 * @description 코드리뷰 서비스 로직을 담당하는 클래스
 */
@TransactionService
public class CodeReviewService extends ContributionService<CodeReview, Long> {

    public CodeReviewService(
            final ContributionRepository<CodeReview, Long> contributionRepository,
            final ContributionMapper<CodeReview> commitMapper) {
        super(contributionRepository, commitMapper);
    }
}
