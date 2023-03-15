package com.dragonguard.backend.config.security.oauth;

import com.dragonguard.backend.config.security.jwt.JwtSetupService;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtSetupService jwtSetupService;
    private final JwtTokenProvider tokenProvider;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        if (response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        jwtSetupService.addJwtTokensInCookie(response, loginUser);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new OAuthProcessingException();
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String accessToken = tokenProvider.createAccessToken((UserDetailsImpl) authentication.getPrincipal()).getAccessToken();

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);

        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort();
    }
}
