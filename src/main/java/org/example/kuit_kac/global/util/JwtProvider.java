package org.example.kuit_kac.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

// 토큰생성, 파싱, 유효성 검사 담당
@Component
@Slf4j
public class JwtProvider {

    private static final String SUB_ACCESS = "access";
    private static final String SUB_REFRESH = "refresh";
    private static final String STAGE_PRE = "pre";    // 온보딩 전
    private static final String STAGE_USER = "user";  // 온보딩 후(일반 보호 API)
    private static final String SCOPE_ONBOARD = "ONBOARD";
    private static final String SCOPE_USER = "USER";

    private final Key accessKey;
    private final Key refreshKey;
    private final long accessTtlMs;
    private final long refreshTtlMs;
    private final long preTtlMs;

    public JwtProvider(
            @Value("${jwt.access-secret-base64}") String accessB64,
            @Value("${jwt.refresh-secret-base64}") String refreshB64,
            @Value("${jwt.access-ttl-ms}") long accessTtlMs,
            @Value("${jwt.refresh-ttl-ms}") long refreshTtlMs,
            @Value("${jwt.pre-access-ttl-ms}") long preTtlMs) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessB64));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshB64));
        this.accessTtlMs = accessTtlMs;
        this.refreshTtlMs = refreshTtlMs;
        this.preTtlMs = preTtlMs;
    }

    // type : "access" || "refresh"
    private String buildToken(
//            Long userId,
//            String kakaoIdOptional,
//            String type,
//            long ttlMs,
//            Key signingKey) {
            Map<String, Object> claims,
            String subject,
            long ttlMs,
            Key signingKey) {
// 기존 키 발급로직
//        Date now = new Date();
//        JwtBuilder b = Jwts.builder().setSubject(type) // 토큰 주제 "access" | "refresh"
//                .setIssuedAt(now) // 발급 시각
//                .setExpiration(new Date(now.getTime() + ttlMs))
//                // 만료 시각
//                .signWith(signingKey, SignatureAlgorithm.HS256); // 서명: 비밀키와 알고리즘
//        if (userId != null) b.claim("uid", userId);
//        if (kakaoIdOptional != null) b.claim("kid", kakaoIdOptional);
//        return b.compact(); // 문자열 토큰으로 변환
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ttlMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generatePreAccessToken(String kakaoId) {
        if (kakaoId == null | kakaoId.isBlank()) throw new CustomException(ErrorCode.ISSUE_TOKEN_KID);
        return buildToken(Map.of(
                "kid", kakaoId,
                "stage", STAGE_PRE,
                "scope", SCOPE_ONBOARD
        ), SUB_ACCESS, preTtlMs, accessKey);
    }

    /**
     * 온보딩 후: uid, kid, ver을 싣는 USER 액세스 토큰 (권한: USER)
     */
    public String generateUserAccessToken(Long userId, String kakaoIdOptional) {
        if (kakaoIdOptional == null || kakaoIdOptional.isBlank()) throw new CustomException(ErrorCode.ISSUE_TOKEN_KID);
        // ✅ uid 없으면 PRE 액세스 토큰으로 자동 다운그레이드 (기존 호출부 호환)
        if (userId == null) {
            return buildToken(Map.of(
                    "kid", kakaoIdOptional,
                    "stage", STAGE_PRE,        // PRE
                    "scope", SCOPE_ONBOARD     // ONBOARD 권한
            ), SUB_ACCESS, preTtlMs, accessKey);
        }
        // ✅ uid 있으면 정상 USER 액세스 토큰
        return buildToken(Map.of(
                "uid", userId,
                "kid", kakaoIdOptional,
                "stage", STAGE_USER,          // USER
                "scope", SCOPE_USER
        ), SUB_ACCESS, accessTtlMs, accessKey);
    }

    public String generateRefreshToken(Long userId) {
        if (userId == null) throw new CustomException(ErrorCode.ISSUE_TOKEN_UID);
        return buildToken(Map.of(
                "uid", userId
        ), SUB_REFRESH, refreshTtlMs, refreshKey);
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
        } catch (Exception ignore) {
        }
        return null;
    }

    public String getKakaoIdFromAccessOrNull(String token) {
        try {
            Object kid = parseAccess(token).get("kid");
            return (kid == null) ? null : String.valueOf(kid);
        } catch (Exception ignore) {
        }
        return null;
    }

    public Long getUserIdFromRefreshOrNull(String token) {
        try {
            Object uid = parseRefresh(token).get("uid");
            if (uid instanceof Number n) return n.longValue();
            if (uid instanceof String s && !s.isBlank()) return Long.parseLong(s);
        } catch (Exception ignore) {
        }
        return null;
    }

    public String getKakaoIdFromRefreshOrNull(String token) {
        try {
            Object kid = parseRefresh(token).get("kid");
            return (kid == null) ? null : String.valueOf(kid);
        } catch (Exception ignore) {
        }
        return null;
    }

    public String getTokenTypeStrict(String token) {
        try {
            return parseWithKey(token, accessKey).getBody().getSubject();
        } catch (JwtException ignore) {
        }
        try {
            return parseWithKey(token, refreshKey).getBody().getSubject();
        } catch (JwtException ignore) {
        }
        return null;
    }

    public boolean isPreAccess(String token) {
        try {
            Claims c = parseAccess(token);
            return STAGE_PRE.equals(String.valueOf(c.get("stage")));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserAccess(String token) {
        try {
            Claims c = parseAccess(token);
            return STAGE_USER.equals(String.valueOf(c.get("stage")));
        } catch (Exception e) {
            return false;
        }
    }


    public String getScopeAccessOrNUll(String token) {
        try {
            Object s = parseAccess(token).get("scooe");
            return (s == null) ? null : String.valueOf(s);
        } catch (Exception ignore) {
            return null;
        }
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

