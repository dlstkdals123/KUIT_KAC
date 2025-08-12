package org.example.kuit_kac.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

// 토큰생성, 파싱, 유효성 검사 담당
@Component
public class JwtProvider {
    private final Key key;
    //    private final String secretKey;
//    private final byte[] keyBytes;
    private long accessTtlMs;
    private long refreshTtlMs;
    private static final Logger LOG = LoggerFactory.getLogger(JwtProvider.class);


    public JwtProvider(
            @Value("${jwt.secret-base64}") String base64Secret,
            @Value("${jwt.access-ttl-ms}") long accessTtlMs,
            @Value("${jwt.refresh-ttl-ms}") long refreshTtlMs
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.accessTtlMs = accessTtlMs;
        this.refreshTtlMs = refreshTtlMs;
    }

    // type : "access" || "refresh"
    private String buildToken(Long userId, String kakaoIdOptional, String type, long ttlMs) {
        Date now = new Date();
        JwtBuilder b = Jwts.builder()
                .setSubject(type) // 토큰 주제 "access" | "refresh"
                .claim("uid", userId) // 서비스 식별자
                .setIssuedAt(now) // 발급 시각
                .setExpiration(new Date(now.getTime() + ttlMs)) // 만료 시각
                .signWith(key, SignatureAlgorithm.HS256); // 서명: 비밀키와 알고리즘
        if (kakaoIdOptional != null) b.claim("kid", kakaoIdOptional);
        return b.compact(); // 문자열 토큰으로 변환
    }

    public String generateAccessToken(Long userId, String kakaoIdOptional) {
        return buildToken(userId, kakaoIdOptional, "access", accessTtlMs);
    }

    public String generateRefreshToken(Long userId) {
        // 필요하면 jti(토큰 ID) 넣어서 블랙리스트/로테이션 관리
        return buildToken(userId, null, "refresh", refreshTtlMs);
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true; // 예외 안 났으면 유효함
        } catch (SecurityException | MalformedJwtException e) {
            // 서명 위조 등 잘못된 토큰
            LOG.warn("JWT invalid signature or malformed: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // 유효기간이 지난 토큰
            LOG.info("JWT expired at {}", e.getClaims().getExpiration());
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 형식의 토큰
            LOG.info("JWT unspported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // 비어 있는 토큰 등
            LOG.info("JWT empty or illeral: {}", e.getMessage());
        }
        return false;
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // key는 이미 JwtProvider에 있는 비밀 키
                .build()
                .parseClaimsJws(token); // 토큰을 파싱하면서 검증도 같이 함
    }

    public Long getUserIdFromToken(String token) {
        Object uid = parse(token).getBody().get("uid");
        if (uid instanceof Integer i) return i.longValue();
        if (uid instanceof Long l) return l;
        if (uid instanceof String s) return Long.parseLong(s);
        throw new IllegalStateException("uid claim missing or invalid");
    }

    public String getTokenType(String token) {
        return parse(token).getBody().getSubject(); // "access" or "refresh"
    }
}
