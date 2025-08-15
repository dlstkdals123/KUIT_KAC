package org.example.kuit_kac.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

// 토큰생성, 파싱, 유효성 검사 담당
@Component
@Slf4j
public class JwtProvider {
    private final Key key;
    //    private final String secretKey;
    //    private final byte[] keyBytes;
    private final long accessTtlMs;
    private final long refreshTtlMs;

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
        return buildToken(userId, null, "refresh", refreshTtlMs);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
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
            log.warn("[JWT] invalid base64url: {}", e.getMessage());} catch (JwtException e) {
                log.warn("[JWT] parse error: {}", e.getMessage());
                return false;
            } catch (Exception e) {
            log.warn("[JWT] invalid token: {}", e.getClass().getSimpleName());
        }
        return false;
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(30) // ← 30초 오차 허용
                .build()
                .parseClaimsJws(token);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdOrNullFromToken(String token) {
        Claims c = parseClaims(token);
        Object uid = c.get("uid");
        if (uid instanceof Number n) return n.longValue();
        if (uid instanceof String s && !s.isBlank()) try {
            return Long.parseLong(s);
        } catch (NumberFormatException ignore) {
        }
        return null;
    }

    public String getKakaoIdOrNullFromToken(String token) {
        Object kid = parseClaims(token).get("kid");
        return kid == null ? null : String.valueOf(kid);
    }

    public String getTokenType(String token) {
        return parse(token).getBody().getSubject(); // "access" or "refresh"
    }

    @PostConstruct
    void logKey() {
        String prefix = Base64.getEncoder().encodeToString(key.getEncoded());
        log.info("[JWT] secret.prefix={}", prefix.substring(0, 12));
    }


}

