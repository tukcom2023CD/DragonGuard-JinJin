package com.dragonguard.backend.domain.search.dto.response;

import lombok.*;

/**
 * @author 김승진
 * @description 유저 검색 결과 응답 정보를 담는 dto
 */

@Getter
@Builder
@ToString // Redis Cache 사용을 위함
@NoArgsConstructor
@AllArgsConstructor
public class UserResultSearchResponse {
    private Long id;
    private String name;
}
