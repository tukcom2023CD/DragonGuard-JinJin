package com.dragonguard.backend.domain.search.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 유저 검색에 대한 Github REST API 응답 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponse {
    private UserResponse[] items;
}
