package com.dragonguard.backend.domain.member.repository;

import static com.dragonguard.backend.domain.member.entity.QMember.member;

import com.dragonguard.backend.domain.member.dto.response.QMemberRankResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 멤버 프로젝션 적용을 위한 클래스
 */
@Component
public class MemberQDtoFactory {

    @Bean
    public QMemberRankResponse qMemberRankResponse() {
        return new QMemberRankResponse(
                member.id,
                member.name,
                member.githubId,
                member.sumOfCommits
                        .add(member.sumOfCodeReviews)
                        .add(member.sumOfIssues)
                        .add(member.sumOfPullRequests)
                        .longValue(),
                member.tier,
                member.profileImage);
    }
}
