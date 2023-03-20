package com.dragonguard.backend.member.service;

import com.dragonguard.backend.config.security.exception.JwtProcessingException;
import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.auth.token.refresh-cookie-key}")
    private String cookieKey;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String refreshToken(String oldRefreshToken, String oldAccessToken) {
        if (!StringUtils.hasText(oldRefreshToken) || !StringUtils.hasText(oldAccessToken)) {
            throw new IllegalArgumentException();
        }

        if (!jwtTokenProvider.validateToken(oldAccessToken)) {
            throw new JwtProcessingException();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        UUID id = UUID.fromString(user.getName());

        String savedToken = memberRepository.findRefreshTokenById(id);

        if (!savedToken.equals(oldRefreshToken)) {
            throw new JwtProcessingException();
        }

        JwtToken token = jwtTokenProvider.createAccessToken(user);

        return token.getAccessToken();
    }

    public Member getLoginUser() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
    }
}
