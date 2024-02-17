package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.jwt.JwtValidator;
import com.dragonguard.backend.config.security.oauth.user.UserPrinciple;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.exception.JwtProcessingException;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 인증 관련 서비스 로직을 담당하는 클래스
 */
@TransactionService
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtValidator jwtValidator;

    public JwtToken refreshToken(final String oldRefreshToken, final String oldAccessToken) {
        validateTokens(oldRefreshToken, oldAccessToken);
        final UserPrinciple userPrinciple = getAuthenticationByToken(oldRefreshToken);

        return getMemberAndUpdateRefreshToken(userPrinciple);
    }

    private JwtToken getMemberAndUpdateRefreshToken(final UserPrinciple userPrinciple) {
        final JwtToken jwtToken = jwtTokenProvider.createToken(userPrinciple);

        memberRepository
                .findById(UUID.fromString(userPrinciple.getName()))
                .orElseThrow(EntityNotFoundException::new)
                .updateRefreshToken(jwtToken.getRefreshToken());

        return jwtToken;
    }

    private void validateTokens(final String oldRefreshToken, final String oldAccessToken) {
        validateJwtTokens(oldRefreshToken, oldAccessToken);
        validateIfRefreshTokenExpired(oldRefreshToken);
    }

    private UserPrinciple getAuthenticationByToken(final String oldRefreshToken) {
        return (UserPrinciple) jwtValidator.getAuthentication(oldRefreshToken).getPrincipal();
    }

    private void validateIfRefreshTokenExpired(final String oldRefreshToken) {
        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new JwtProcessingException();
        }
    }

    private void validateJwtTokens(final String oldRefreshToken, final String oldAccessToken) {
        if (isEmptyToken(oldRefreshToken, oldAccessToken)) {
            throw new JwtProcessingException();
        }
    }

    private boolean isEmptyToken(final String oldRefreshToken, final String oldAccessToken) {
        return !StringUtils.hasText(oldRefreshToken) || !StringUtils.hasText(oldAccessToken);
    }

    public UUID getLoginUserId() {
        final Object principal =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(((UserPrinciple) principal).getName());
    }

    public Member getLoginUser() {
        return memberRepository
                .findById(getLoginUserId())
                .orElseThrow(EntityNotFoundException::new);
    }
}
