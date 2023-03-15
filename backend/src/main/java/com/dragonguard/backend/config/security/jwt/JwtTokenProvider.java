package com.dragonguard.backend.config.security.jwt;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Key key;
    private final Long ACCESS_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 1000; // 1 Day
    private final Long REFRESH_TOKEN_EXPIRE_LENGTH = 60L * 60 * 24 * 14 * 1000; // 14 Days
    private final MemberRepository memberRepository;

    public JwtToken createAccessToken(UserDetailsImpl userDetails) {
        Claims claims = getClaims(userDetails);

        String accessToken = getToken(userDetails, claims, ACCESS_TOKEN_EXPIRE_LENGTH);
        String refreshToken = getToken(userDetails, claims, REFRESH_TOKEN_EXPIRE_LENGTH);

        saveRefreshToken(refreshToken, userDetails);

        return JwtToken.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveRefreshToken(String refreshToken, UserDetailsImpl userDetails) {
        UUID id = UUID.fromString(userDetails.getUsername());

        memberRepository.updateRefreshToken(id, refreshToken);
    }

    private Claims getClaims(UserDetailsImpl userDetails) {
        Claims claims = Jwts.claims();
        claims.put("id", userDetails.getName());
        return claims;
    }

    private String getToken(UserDetailsImpl loginUser, Claims claims, Long validationSecond) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(loginUser.getName())
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + validationSecond))
                .compact();
    }
}
