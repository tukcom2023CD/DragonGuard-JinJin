package com.dragonguard.backend.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author 김승진
 * @description JWT의 정보를 담는 클래스
 */

@Getter
@Builder
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
