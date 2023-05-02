package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.member.exception.JwtProcessingException;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.jwt.JwtValidator;
import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 인증 관련 서비스 로직을 담당하는 클래스
 */

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtValidator jwtValidator;

    @Transactional
    public JwtToken refreshToken(String oldRefreshToken, String oldAccessToken) {
        if (!StringUtils.hasText(oldRefreshToken) || !StringUtils.hasText(oldAccessToken)) {
            throw new IllegalArgumentException();
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
}
