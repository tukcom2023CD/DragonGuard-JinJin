package com.dragonguard.backend.domain.contribution.mapper;

import com.dragonguard.backend.domain.contribution.dto.request.CommitScrapingRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ContributionMapper {
    public CommitScrapingRequest toRequest(String githubId) {
        return CommitScrapingRequest.builder()
                .githubId(githubId)
                .year(LocalDate.now().getYear())
                .build();
    }
}
