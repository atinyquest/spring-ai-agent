package com.tinyquest.exam.config.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "MFh+Qo+ZzLxv+9GB+NsHZtP9wLCDrQq6zg8+LnK/cd8="; // 256비트 이상 필요
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final SecretKey key;

    public JwtTokenProvider() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // **JWT 액세스 토큰 생성**
    public String generateAccessToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key) // SignatureAlgorithm 지정 없이 키만 사용
                .compact();
    }

    // **JWT 리프레쉬 토큰 생성**
    public String generateRefreshToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key) // SignatureAlgorithm 지정 없이 키만 사용
                .compact();
    }

    // **JWT 토큰에서 사용자명 추출**
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // **JWT 토큰 검증**
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // **토큰의 클레임 정보 파싱**
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
