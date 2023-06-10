package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.jwt.JwtValidator;
import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.exception.CookieException;
import com.dragonguard.backend.domain.member.exception.JwtProcessingException;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
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
        if (!StringUtils.hasText(oldRefreshToken) || !StringUtils.hasText(oldAccessToken)) {
            throw new CookieException();
        }

        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new JwtProcessingException();
        }

        Authentication authentication = jwtValidator.getAuthentication(oldAccessToken);
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        UUID id = UUID.fromString(user.getName());

        String savedToken = memberRepository.findRefreshTokenById(id);

        if (!savedToken.equals(oldRefreshToken)) {
            throw new JwtProcessingException();
        }

        JwtToken jwtToken = jwtTokenProvider.createToken(user);

        Member member = user.getMember();
        member.updateRefreshToken(jwtToken.getRefreshToken());

        return jwtToken;
    }

    public Member getLoginUser() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
    }

    public UUID getLoginUserId() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember().getId();
    }
}
