package com.dragonguard.backend.issue.mapper;

import com.dragonguard.backend.issue.entity.Issue;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description issue Entity와 dto 사이의 변환을 돕는 클래스
 */

@Component
public class IssueMapper {
    public Issue toEntity(String githubId, Integer issueNum, Integer year) {
        return Issue.builder()
                .githubId(githubId)
                .issueNum(issueNum)
                .year(year)
                .build();
    }
}
