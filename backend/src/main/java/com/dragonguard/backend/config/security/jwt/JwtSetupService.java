package com.dragonguard.backend.config.security.jwt;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class JwtSetupService {
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.auth.token.access-header}")
    private String accessTokenHeaderTag;

    @Value("${app.auth.token.refresh-header}")
    private String refreshTokenHeaderTag;

    public JwtSetupService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void addJwtTokensInCookie(HttpServletResponse response, UserDetailsImpl loginUser) {
        JwtToken jwtToken = jwtTokenProvider.createAccessToken(loginUser);
        ResponseCookie accessTokenCookie =
                setCookie(accessTokenHeaderTag, jwtToken.getAccessToken());
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        ResponseCookie refreshTokenCookie =
                setCookie(refreshTokenHeaderTag, jwtToken.getRefreshToken());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    private ResponseCookie setCookie(String key, String value) {
        return ResponseCookie.from(key, value).path("/").httpOnly(true).build();
    }
}
