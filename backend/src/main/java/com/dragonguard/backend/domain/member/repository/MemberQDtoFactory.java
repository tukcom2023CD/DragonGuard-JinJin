package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.dto.response.QMemberRankResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.dragonguard.backend.domain.member.entity.QMember.member;


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
                member.tier,
                member.profileImage);
    }
}
