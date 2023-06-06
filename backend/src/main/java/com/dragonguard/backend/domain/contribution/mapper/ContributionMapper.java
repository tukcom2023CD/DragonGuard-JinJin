package com.dragonguard.backend.domain.contribution.mapper;

import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ContributionMapper {
    public ContributionScrapingRequest toRequest(final String githubId) {
        return ContributionScrapingRequest.builder()
                .githubId(githubId)
                .year(LocalDate.now().getYear())
                .build();
    }
}
