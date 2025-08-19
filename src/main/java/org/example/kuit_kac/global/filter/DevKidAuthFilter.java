package org.example.kuit_kac.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.global.util.dev.DevKidAuthProperties;
import org.example.kuit_kac.global.util.dev.DevWhitelistProperties;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Component
@Profile({"local","dev"})
@RequiredArgsConstructor
@Slf4j
public class DevKidAuthFilter extends OncePerRequestFilter {

    private final DevKidAuthProperties props;
    private final DevWhitelistProperties whitelist;
    private final JwtProvider jwtProvider;          // 🔸 추가: access 파싱용

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!props.isEnabled()) return true;
        String p = request.getRequestURI();
        return p.startsWith("/oauth2/")
                || p.startsWith("/login/oauth2/")
                || p.startsWith("/swagger-ui/")
                || p.startsWith("/v3/api-docs/")
                || p.startsWith("/h2-console/")
                || p.startsWith("/dev-tools/")
                || p.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // 이미 인증 있으면 패스
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(req, res);
            return;
        }

        String kid = null;
        Long uid = null;

        // 1) Bearer access에서 kid/uid 뽑기
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                if ("access".equals(jwtProvider.getTokenTypeStrict(token)) && jwtProvider.validateToken(token)) {
                    kid = jwtProvider.getKakaoIdFromAccessOrNull(token);
                    uid = jwtProvider.getUserIdFromAccessOrNull(token);
                }
            } catch (Exception ignore) {}
        }

        // 2) DEV 서명 헤더 폴백
        if ((kid == null || kid.isBlank())) {
            String headerKid = req.getHeader("X-KID");
            String ts  = req.getHeader("X-TS");
            String sig = req.getHeader("X-SIG");
            if (headerKid != null && ts != null && sig != null) {
                try {
                    long tsv = Long.parseLong(ts);
                    long now = Instant.now().getEpochSecond();
                    boolean fresh = Math.abs(now - tsv) <= props.getTsSkewSeconds();
                    String expected = hmacSha256Url(props.getSecret(), headerKid + "|" + ts);
                    if (fresh && constantTimeEquals(expected, sig)) {
                        kid = headerKid;
                    } else {
                        log.debug("[DevKidAuth] HMAC/TS invalid: fresh={}, expected={}, got={}, kid={}, ts={}",
                                fresh, preview(expected), preview(sig), headerKid, ts);
                    }
                } catch (Exception e) {
                    log.debug("[DevKidAuth] header parse error: {}", e.toString());
                }
            }
        }

        // 3) (옵션) 쿼리 파라미터 kid 폴백 - 로컬 디버그용
        if (kid == null || kid.isBlank()) {
            String qp = req.getParameter("kid");
            if (qp != null && !qp.isBlank()) kid = qp;
        }

        // 최종 주입
        if (kid != null && !kid.isBlank()) {
            if (whitelist.isEnabled() && !whitelist.allows(kid)) {
                log.info("[DevKidAuth] reject by whitelist: kid={}", kid);
            } else {
                // ✅ dev 우회 Principal: 약관/온보딩 통과
                UserPrincipal principal = UserPrincipal.devBypass(uid, kid);

                var authToken = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_USER"),
                                new SimpleGrantedAuthority("ROLE_DEV")
                        )
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("[DevKidAuth] accept kid={}, uid={}, role=ROLE_DEV", kid, uid);
            }
        }

        chain.doFilter(req, res);
    }

    private static String hmacSha256Url(String secret, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int r = 0;
        for (int i = 0; i < a.length(); i++) r |= a.charAt(i) ^ b.charAt(i);
        return r == 0;
    }

    private static String preview(String s) {
        if (s == null) return "null";
        int n = Math.min(10, s.length());
        return s.substring(0, n) + "...(len=" + s.length() + ")";
    }
}
