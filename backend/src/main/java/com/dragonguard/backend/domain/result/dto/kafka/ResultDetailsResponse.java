package com.dragonguard.backend.domain.result.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 검색 결과를 보여주는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDetailsResponse {
    private String name;
}
