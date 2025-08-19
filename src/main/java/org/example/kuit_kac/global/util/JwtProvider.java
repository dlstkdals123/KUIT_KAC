package org.example.kuit_kac.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

// 토큰생성, 파싱, 유효성 검사 담당
@Component
@Slf4j
public class JwtProvider {
    private final Key accessKey;
    private final Key refreshKey;
    private final long accessTtlMs;
    private final long refreshTtlMs;

    public JwtProvider(
            @Value("${jwt.access-secret-base64}") String accessB64,
            @Value("${jwt.refresh-secret-base64}") String refreshB64,
            @Value("${jwt.access-ttl-ms}") long accessTtlMs,
            @Value("${jwt.refresh-ttl-ms}") long refreshTtlMs) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessB64));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshB64));
        this.accessTtlMs = accessTtlMs;
        this.refreshTtlMs = refreshTtlMs;
    }

    // type : "access" || "refresh"
    private String buildToken(Long userId, String kakaoIdOptional, String type, long ttlMs, Key signingKey) {
        Date now = new Date();
        JwtBuilder b = Jwts.builder().setSubject(type) // 토큰 주제 "access" | "refresh"
                .setIssuedAt(now) // 발급 시각
                .setExpiration(new Date(now.getTime() + ttlMs))
                // 만료 시각
                .signWith(signingKey, SignatureAlgorithm.HS256); // 서명: 비밀키와 알고리즘
        if (userId != null) b.claim("uid", userId);
        if (kakaoIdOptional != null) b.claim("kid", kakaoIdOptional);
        return b.compact(); // 문자열 토큰으로 변환
    }

    public String generateAccessToken(Long userId, String kakaoIdOptional) {
        return buildToken(userId, kakaoIdOptional, "access", accessTtlMs, accessKey);
    }

    public String generateRefreshToken(Long userId) {
        return buildToken(userId, null, "refresh", refreshTtlMs, refreshKey);
    }

    public boolean validateAccess(String token) {
        try {
            Claims c = parseWithKey(token, accessKey).getBody();
            return "access".equals(c.getSubject());
        } catch (ExpiredJwtException e) {
            log.info("[JWT-ACCESS] expired at {}", e.getClaims().getExpiration());
        } catch (JwtException e) {
            log.info("[JWT-ACCESS] invalid: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateRefresh(String token) {
        try {
            Claims c = parseWithKey(token, refreshKey).getBody();
            return "refresh".equals(c.getSubject());
        } catch (ExpiredJwtException e) {
            log.info("[JWT-REFRESH] expired at {}", e.getClaims().getExpiration());
        } catch (JwtException e) {
            log.info("[JWT-REFRESH] invalid: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateToken(String token) {
        try {
            parseClaimsEither(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("[JWT] expired at {}", e.getClaims().getExpiration());
        } catch (io.jsonwebtoken.security.SignatureException | SecurityException | MalformedJwtException e) {
            log.warn("[JWT] signature invalid / malformed: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("[JWT] unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("[JWT] empty/illegal: {}", e.getMessage());
        } catch (DecodingException e) {
            log.warn("[JWT] invalid base64url: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("[JWT] parse error: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("[JWT] invalid token: {}", e.getClass().getSimpleName());
        }
        return false;
    }

    public Claims parseAccess(String token) {
        Claims c = parseWithKey(token, accessKey).getBody();
        if (!"access".equals(c.getSubject())) throw new JwtException("Not an access token");
        return c;
    }

    public Claims parseRefresh(String token) {
        Claims c = parseWithKey(token, refreshKey).getBody();
        if (!"refresh".equals(c.getSubject())) throw new JwtException("Not a refresh token");
        return c;
    }

    public Claims parseClaimsEither(String token) {
        try {
            return parseWithKey(token, accessKey).getBody();
        } catch (JwtException ignore) {
            return parseWithKey(token, refreshKey).getBody();
        }
    }

    public Long getUserIdFromAccessOrNull(String token) {
        try {
            Object uid = parseAccess(token).get("uid");
            if (uid instanceof Number n) return n.longValue();
            if (uid instanceof String s && !s.isBlank()) return Long.parseLong(s);
        } catch (Exception ignore) {}
        return null;
    }

    public String getKakaoIdFromAccessOrNull(String token) {
        try {
            Object kid = parseAccess(token).get("kid");
            return (kid == null) ? null : String.valueOf(kid);
        } catch (Exception ignore) {}
        return null;
    }

    public Long getUserIdFromRefreshOrNull(String token) {
        try {
            Object uid = parseRefresh(token).get("uid");
            if (uid instanceof Number n) return n.longValue();
            if (uid instanceof String s && !s.isBlank()) return Long.parseLong(s);
        } catch (Exception ignore) {}
        return null;
    }

    public String getKakaoIdFromRefreshOrNull(String token) {
        try {
            Object kid = parseRefresh(token).get("kid");
            return (kid == null) ? null : String.valueOf(kid);
        } catch (Exception ignore) {}
        return null;
    }

    public String getTokenTypeStrict(String token) {
        try { return parseWithKey(token, accessKey).getBody().getSubject(); } catch (JwtException ignore) {}
        try { return parseWithKey(token, refreshKey).getBody().getSubject(); } catch (JwtException ignore) {}
        return null;
    }

    @PostConstruct
    void logKey() {
        String aPrefix = Base64.getEncoder().encodeToString(((SecretKey) accessKey).getEncoded());
        String rPrefix = Base64.getEncoder().encodeToString(((SecretKey) refreshKey).getEncoded());
        aPrefix = aPrefix.substring(0, Math.min(12, aPrefix.length()));
        rPrefix = rPrefix.substring(0, Math.min(12, rPrefix.length()));
        log.info("[JWT] access.prefix={} refresh.prefix={}", aPrefix, rPrefix);
    }

    private Jws<Claims> parseWithKey(String token, Key k) {
        return Jwts.parserBuilder()
                .setSigningKey(k)
                .setAllowedClockSkewSeconds(30)
                .build()
                .parseClaimsJws(token);
    }
}

