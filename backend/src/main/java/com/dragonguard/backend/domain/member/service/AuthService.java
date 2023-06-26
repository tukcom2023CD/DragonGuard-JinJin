package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.jwt.JwtValidator;
import com.dragonguard.backend.config.security.oauth.user.UserPrinciple;
import com.dragonguard.backend.domain.member.exception.JwtProcessingException;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

        UserPrinciple user = getUserDetails(oldAccessToken);

        validateSavedRefreshTokenIfExpired(oldRefreshToken, UUID.fromString(user.getName()));

        return findMemberAndUpdateRefreshToken(user);
    }

    private JwtToken findMemberAndUpdateRefreshToken(final UserPrinciple user) {
        JwtToken jwtToken = jwtTokenProvider.createToken(user);

        memberRepository.findById(UUID.fromString(((UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName()))
                .orElseThrow(EntityNotFoundException::new)
                .updateRefreshToken(jwtToken.getRefreshToken());
        return jwtToken;
    }

    private UserPrinciple getUserDetails(final String oldAccessToken) {
        Authentication authentication = jwtValidator.getAuthentication(oldAccessToken);
        return (UserPrinciple) authentication.getPrincipal();
    }

    private void validateTokens(final String oldRefreshToken, final String oldAccessToken) {
        validateJwtTokens(oldRefreshToken, oldAccessToken);
        validateIfRefreshTokenExpired(oldRefreshToken);
    }

    private void validateSavedRefreshTokenIfExpired(final String oldRefreshToken, final UUID id) {
        String savedToken = memberRepository.findRefreshTokenById(id);
        if (!savedToken.equals(oldRefreshToken)) {
            throw new JwtProcessingException();
        }
    }

    private void validateIfRefreshTokenExpired(final String oldRefreshToken) {
        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new JwtProcessingException();
        }
    }

    private void validateJwtTokens(final String oldRefreshToken, final String oldAccessToken) {
        if (!StringUtils.hasText(oldRefreshToken) || !StringUtils.hasText(oldAccessToken)) {
            throw new JwtProcessingException();
        }
    }

    public UUID getLoginUserId() {
        return UUID.fromString(((UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getName());
    }
}
