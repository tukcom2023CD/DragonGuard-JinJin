package com.dragonguard.backend.config.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtValidator jwtValidator;
    private final String tokenTag;

    public JwtAuthenticationFilter(JwtValidator jwtValidator, @Value("${app.auth.token.access-header}") String tokenTag) {
        this.jwtValidator = jwtValidator;
        this.tokenTag = tokenTag;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request.getCookies());

        if (StringUtils.hasText(token)) {
            Authentication authentication = jwtValidator.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        Optional<Cookie> accessCookie = extractAccessToken(cookies);
        return accessCookie.map(Cookie::getValue).orElse(null);
    }

    private Optional<Cookie> extractAccessToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(tokenTag))
                .findFirst();
    }
}
