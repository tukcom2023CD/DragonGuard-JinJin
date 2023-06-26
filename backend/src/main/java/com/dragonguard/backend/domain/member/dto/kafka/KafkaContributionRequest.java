package com.dragonguard.backend.domain.member.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 멤버 기여도 Kafka 요청 정보를 갖는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaContributionRequest {
    private String githubId;
}
