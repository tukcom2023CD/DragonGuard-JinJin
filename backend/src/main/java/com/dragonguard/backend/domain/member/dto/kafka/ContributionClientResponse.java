package com.dragonguard.backend.domain.member.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 멤버 기여도 Client Kafka 응답 정보를 갖는 dto 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContributionClientResponse {
    private String githubId;
}
