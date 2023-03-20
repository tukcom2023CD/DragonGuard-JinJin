package com.dragonguard.backend.member.service;

import com.dragonguard.backend.config.security.exception.JwtProcessingException;
import com.dragonguard.backend.config.security.jwt.JwtSetupService;
import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.config.security.jwt.JwtTokenProvider;
import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtSetupService jwtSetupService;

    @Transactional
    public String refreshToken(String oldRefreshToken, String oldAccessToken, HttpServletResponse response) {
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

        JwtToken jwtToken = jwtSetupService.addJwtTokensInCookie(response, user);
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        member.updateRefreshToken(jwtToken.getRefreshToken());

        return jwtToken.getAccessToken();
    }

    public Member getLoginUser() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
    }
}
