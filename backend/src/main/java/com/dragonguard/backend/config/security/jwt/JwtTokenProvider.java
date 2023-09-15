package com.dragonguard.backend.config.security.jwt;

import com.dragonguard.backend.config.security.oauth.user.UserPrinciple;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * @author 김승진
 * @description JWT 토큰을 발급하는 클래스
 */

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final Long ACCESS_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 1000; // 1 Day
    private static final Long REFRESH_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 14 * 1000; // 14 Days
    private static final String USER_ID_CLAIM_NAME = "id";
    private final MemberRepository memberRepository;
    private final Key key;

    public JwtToken createToken(final UserPrinciple userDetails) {
        final String accessToken = getToken(userDetails, getClaims(userDetails), ACCESS_TOKEN_EXPIRE_LENGTH);
        final String refreshToken = getToken(userDetails, getClaims(userDetails), REFRESH_TOKEN_EXPIRE_LENGTH);

        saveRefreshToken(refreshToken, userDetails);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | IllegalStateException e) {
            return false;
        }
    }

    private void saveRefreshToken(final String refreshToken, final UserPrinciple userDetails) {
        final UUID id = UUID.fromString(userDetails.getName());

        memberRepository.updateRefreshToken(id, refreshToken);
    }

    private Claims getClaims(final UserPrinciple userDetails) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID_CLAIM_NAME, userDetails.getName());
        return claims;
    }

    private String getToken(final UserPrinciple loginUser, final Claims claims, final Long validationSecond) {
        final long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(loginUser.getName())
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + validationSecond))
                .compact();
    }
}
