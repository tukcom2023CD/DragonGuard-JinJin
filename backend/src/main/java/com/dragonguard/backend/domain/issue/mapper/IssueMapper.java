package com.dragonguard.backend.domain.issue.mapper;

import com.dragonguard.backend.domain.issue.entity.Issue;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description issue Entity와 dto 사이의 변환을 돕는 클래스
 */

@Component
public class IssueMapper {
    public Issue toEntity(final String githubId, final Integer issueNum, final Integer year) {
        return Issue.builder()
                .githubId(githubId)
                .amount(issueNum)
                .year(year)
                .build();
    }
}
