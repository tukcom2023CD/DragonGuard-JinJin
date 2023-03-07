package com.dragonguard.backend.result.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 검색 결과 응답 정보를 담는 dto
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    private Long id;
    private String name;
}
