package com.dragonguard.backend.config.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description OAuth2의 요청 데이터를 담당하는 클래스
 */

@Getter
@AllArgsConstructor
public class OAuth2Request {
    private String accountId;
}
