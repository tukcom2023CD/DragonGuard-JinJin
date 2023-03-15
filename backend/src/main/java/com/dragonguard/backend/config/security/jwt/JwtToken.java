package com.dragonguard.backend.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
    private String grantType;
}
