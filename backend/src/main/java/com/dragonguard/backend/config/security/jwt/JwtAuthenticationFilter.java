package com.dragonguard.backend.config.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author 김승진
 * @description JWT 토큰의 유효성에 따라 로직을 수행하는 필터 클래스
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtValidator jwtValidator;
    @Value("${app.auth.token.access-header}")
    private String tokenTag;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = Optional.ofNullable(extractToken(request.getCookies()));

        token.ifPresent(
                t -> {
                    Authentication authentication = jwtValidator.getAuthentication(t);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

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
