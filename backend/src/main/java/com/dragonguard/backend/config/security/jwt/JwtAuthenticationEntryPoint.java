package com.dragonguard.backend.config.security.jwt;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 인증이 되지 않은 JWT 관련 로직 수행 클래스
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException)
            throws IOException {
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
    }
}
