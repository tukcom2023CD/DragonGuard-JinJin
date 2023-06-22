package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 요청 정보를 담는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SparkLineKafka {
    private String githubToken;
    private Long id;
}
