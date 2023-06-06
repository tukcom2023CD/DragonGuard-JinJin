package com.dragonguard.backend.domain.commit.mapper;

import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.domain.commit.entity.Commit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 커밋 Entity 와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class CommitMapper {

    public Commit toEntity(final ContributionScrapingResponse contributionScrapingResponse) {
        return Commit.builder()
                .amount(contributionScrapingResponse.getCommitNum())
                .year(LocalDate.now().getYear())
                .githubId(contributionScrapingResponse.getGithubId())
                .build();
    }
}
