package com.dragonguard.backend.config.security.jwt;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.auth.token.access-header}")
    private String accessTokenHeaderTag;

    @Value("${app.auth.token.refresh-header}")
    private String refreshTokenHeaderTag;

    public void addJwtTokensInCookie(HttpServletResponse response, UserDetailsImpl loginUser) {
        JwtToken jwtToken = jwtTokenProvider.createToken(loginUser);
        ResponseCookie accessTokenCookie =
                setCookie(accessTokenHeaderTag, jwtToken.getAccessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        ResponseCookie refreshTokenCookie =
                setCookie(refreshTokenHeaderTag, jwtToken.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private ResponseCookie setCookie(String key, String value) {
        return ResponseCookie.from(key, value).path("/").httpOnly(true).build();
    }
}
