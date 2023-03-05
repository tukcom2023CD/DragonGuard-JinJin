package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.dto.response.QMemberRankResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.dragonguard.backend.member.entity.QMember.member;

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
                member.sumOfTokens,
                member.tier);
    }
}
