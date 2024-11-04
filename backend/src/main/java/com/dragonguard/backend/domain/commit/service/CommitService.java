package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.template.mapper.ContributionMapper;
import com.dragonguard.backend.global.template.repository.ContributionRepository;
import com.dragonguard.backend.global.template.service.ContributionService;

/**
 * @author 김승진
 * @description 커밋과 관련된 서비스 로직을 처리하는 클래스
 */
@TransactionService
public class CommitService extends ContributionService<Commit, Long> {
    public CommitService(
            final ContributionRepository<Commit, Long> contributionRepository,
            final ContributionMapper<Commit> commitMapper) {
        super(contributionRepository, commitMapper);
    }
}
