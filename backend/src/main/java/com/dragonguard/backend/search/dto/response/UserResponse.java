package com.dragonguard.backend.search.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 유저 검색에 대한 Github REST API 응답 정보(깃허브 아이디)를 담는 dto
 */

@Getter
@AllArgsConstructor
public class UserResponse {
    private String login;
}
