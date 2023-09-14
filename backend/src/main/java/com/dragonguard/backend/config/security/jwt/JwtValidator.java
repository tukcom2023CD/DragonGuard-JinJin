package com.dragonguard.backend.config.security.jwt;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.config.security.oauth.user.UserPrinciple;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 김승진
 * @description JWT 토큰 정보를 검증하는 클래스
 */

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private static final String USER_ID_CLAIM_NAME = "id";
    private static final String EMPTY_CREDENTIAL = "";
    private final MemberRepository memberRepository;
    private final UserDetailsMapper userDetailsMapper;
    private final Key key;

    @Transactional
    public Authentication getAuthentication(final String accessToken) {
        final Claims claims = getTokenBodyClaims(accessToken);

        return memberRepository.findById(extractUUID(claims))
                .map(userDetailsMapper::mapToLoginUser)
                .map(loginUser -> new UsernamePasswordAuthenticationToken(loginUser, EMPTY_CREDENTIAL, loginUser.getAuthorities()))
                .orElse(null);
    }

    public UUID extractUUID(final Claims claims) {
        return UUID.fromString(claims.get(USER_ID_CLAIM_NAME, String.class));
    }

    public Claims getTokenBodyClaims(final String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
