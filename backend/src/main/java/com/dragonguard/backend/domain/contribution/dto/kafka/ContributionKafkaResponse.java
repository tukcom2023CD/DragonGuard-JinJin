package com.dragonguard.backend.domain.contribution.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 카프카로 기여도를 가져오기위해 쓰이는 정보들을 묶은 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContributionKafkaResponse {
    private String githubId;
    private String name;
    private Integer contribution;
    private String profileImage;
}
