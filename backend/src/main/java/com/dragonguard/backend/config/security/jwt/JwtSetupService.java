package com.dragonguard.backend.config.security.jwt;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 김승진
 * @description JWT와 쿠키의 연계를 돕는 클래스
 */
@Service
@RequiredArgsConstructor
public class JwtSetupService {
    private static final String DEFAULT_PATH = "/";

    @Value("${app.auth.token.access-header}")
    private String accessTokenHeaderTag;

    @Value("${app.auth.token.refresh-header}")
    private String refreshTokenHeaderTag;

    public void addJwtTokensInCookie(final HttpServletResponse response, final JwtToken jwtToken) {
        addCookie(response, jwtToken.getAccessToken(), accessTokenHeaderTag);
        addCookie(response, jwtToken.getRefreshToken(), refreshTokenHeaderTag);
    }

    private void addCookie(
            final HttpServletResponse response, final String token, final String tag) {
        final ResponseCookie tokenCookie = setCookie(tag, token);
        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
    }

    private ResponseCookie setCookie(final String key, final String value) {
        return ResponseCookie.from(key, value).path(DEFAULT_PATH).httpOnly(true).build();
    }
}
