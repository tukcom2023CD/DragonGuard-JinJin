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
    private final Key key;
    private static final Long ACCESS_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 1000; // 1 Day
    private static final Long REFRESH_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 14 * 1000; // 14 Days
    private final MemberRepository memberRepository;

    public JwtToken createToken(UserPrinciple userDetails) {
        Claims accessClaims = getAccessClaims(userDetails);
        Claims refreshClaims = getRefreshClaims();

        String accessToken = getToken(userDetails, accessClaims, ACCESS_TOKEN_EXPIRE_LENGTH);
        String refreshToken = getToken(userDetails, refreshClaims, REFRESH_TOKEN_EXPIRE_LENGTH);

        saveRefreshToken(refreshToken, userDetails);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | IllegalStateException e) {
            return false;
        }
    }

    private void saveRefreshToken(String refreshToken, UserPrinciple userDetails) {
        UUID id = UUID.fromString(userDetails.getName());

        memberRepository.updateRefreshToken(id, refreshToken);
    }

    private Claims getAccessClaims(UserPrinciple userDetails) {
        Claims claims = Jwts.claims();
        claims.put("id", userDetails.getName());
        return claims;
    }

    private Claims getRefreshClaims() {
        return Jwts.claims();
    }

    private String getToken(UserPrinciple loginUser, Claims claims, Long validationSecond) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(loginUser.getName())
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + validationSecond))
                .compact();
    }
}
