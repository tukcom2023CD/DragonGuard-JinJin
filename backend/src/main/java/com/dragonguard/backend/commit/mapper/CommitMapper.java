package com.dragonguard.backend.commit.mapper;

import com.dragonguard.backend.commit.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.commit.dto.response.CommitScrapingResponse;
import com.dragonguard.backend.commit.entity.Commit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author 김승진
 * @description 커밋 Entity 와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class CommitMapper {

    public Commit toEntity(CommitScrapingResponse commitScrapingResponse) {
        return Commit.builder()
                .amount(commitScrapingResponse.getCommitNum())
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
