package com.dragonguard.backend.domain.result.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author 김승진
 * @description 유저 검색 결과 응답 정보를 담는 dto
 */

@Getter
@Builder
@AllArgsConstructor
public class UserResultResponse {
    private Long id;
    private String name;
}
