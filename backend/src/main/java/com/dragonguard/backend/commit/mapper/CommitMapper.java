package com.dragonguard.backend.commit.mapper;

import com.dragonguard.backend.commit.dto.request.CommitScrappingRequest;
import com.dragonguard.backend.commit.dto.response.CommitScrappingResponse;
import com.dragonguard.backend.commit.entity.Commit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommitMapper {

    public Commit toEntity(CommitScrappingResponse commitScrappingResponse) {
        return Commit.builder()
                .commitNum(commitScrappingResponse.getCommitNum())
                .year(LocalDate.now().getYear())
                .githubId(commitScrappingResponse.getGithubId())
                .build();
    }

    public CommitScrappingRequest toRequest(String githubId) {
        return CommitScrappingRequest.builder()
                .githubId(githubId)
                .year(LocalDate.now().getYear())
                .build();
    }
}
