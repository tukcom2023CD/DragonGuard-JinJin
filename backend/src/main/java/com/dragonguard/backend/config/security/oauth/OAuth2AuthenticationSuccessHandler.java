package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.jwt.JwtSetupService;
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
    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtSetupService jwtSetupService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUri = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);

        UserPrinciple loginUser = (UserPrinciple) authentication.getPrincipal();
        jwtSetupService.addJwtTokensInCookie(response, loginUser);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String accessToken = tokenProvider.createToken((UserPrinciple) authentication.getPrincipal()).getAccessToken();

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
    }
}
