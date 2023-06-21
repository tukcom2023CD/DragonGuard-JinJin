package com.dragonguard.backend.domain.result.dto.response;

import lombok.*;

/**
 * @author 김승진
 * @description 유저 검색 결과 응답 정보를 담는 dto
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResultResponse {
    private Long id;
    private String name;
}
