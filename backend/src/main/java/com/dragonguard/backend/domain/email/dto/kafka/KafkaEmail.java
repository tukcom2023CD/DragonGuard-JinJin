package com.dragonguard.backend.domain.email.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 이메일을 카프카를 통해 보낼 때 필요한 정보들을 담는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEmail {
    private String memberEmail;
    private Integer random;
}
