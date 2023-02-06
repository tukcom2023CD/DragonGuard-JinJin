package com.dragonguard.backend.commit.mapper;

import com.dragonguard.backend.commit.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.commit.dto.response.CommitScrapingResponse;
import com.dragonguard.backend.commit.entity.Commit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommitMapper {

    public Commit toEntity(CommitScrapingResponse commitScrapingResponse) {
        return Commit.builder()
                .commitNum(commitScrapingResponse.getCommitNum())
                .year(LocalDate.now().getYear())
                .githubId(commitScrapingResponse.getGithubId())
                .build();
    }

    public CommitScrapingRequest toRequest(String githubId) {
        return CommitScrapingRequest.builder()
                .githubId(githubId)
                .year(LocalDate.now().getYear())
                .build();
    }
}
