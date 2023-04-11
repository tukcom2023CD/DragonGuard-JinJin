package com.dragonguard.backend.pullrequest.mapper;

import com.dragonguard.backend.pullrequest.entity.PullRequest;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description PullRequest Entity와 dto 사이의 변환을 돕는 클래스
 */

@Component
public class PullRequestMapper {
    public PullRequest toEntity(String githubId, Integer pullRequestNum, Integer year) {
        return PullRequest.builder()
                .githubId(githubId)
                .amount(pullRequestNum)
                .year(year)
                .build();
    }
}
