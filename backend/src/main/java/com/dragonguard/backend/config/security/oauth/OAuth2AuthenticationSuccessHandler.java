package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.jwt.JwtSetupService;
import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.oauth.user.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 김승진
 * @description OAuth2 인증 성공 로직을 수행할 클래스
 */

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String REFRESH_TAG = "refresh";
    private static final String ACCESS_TAG = "access";

    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtSetupService jwtSetupService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
            throws IOException {
        if (response.isCommitted()) {
            return;
        }
        super.clearAuthenticationAttributes(request);

        final UserPrinciple loginUser = (UserPrinciple) authentication.getPrincipal();
        final JwtToken jwtToken = jwtTokenProvider.createToken(loginUser);
        final String targetUri = determineTargetUrl(jwtToken);

        jwtSetupService.addJwtTokensInCookie(response, jwtToken);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private String determineTargetUrl(final JwtToken jwtToken) {
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam(ACCESS_TAG, jwtToken.getAccessToken())
                .queryParam(REFRESH_TAG, jwtToken.getRefreshToken())
                .build().toUriString();
    }
}
