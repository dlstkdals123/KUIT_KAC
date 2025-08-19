package org.example.kuit_kac.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.global.util.dev.DevWhitelistProperties;
import org.example.kuit_kac.domain.terms.service.UserTermsService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserService userService; // 최신값 필요하면 조회해서 Principal 새로 만듦
    private final UserTermsService userTermsService;
    private final DevWhitelistProperties devWhitelistProperties;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String p = request.getServletPath();
        return p.startsWith("/oauth2/") // 로그인 시작
                || p.startsWith("/login/oauth2/") // 콜백
                || p.startsWith("/swagger-ui/")
                || p.startsWith("/v3/api-docs/")
                || p.equals("/") || p.startsWith("/health") || p.startsWith("/actuator");
//                || p.startsWith("/reset-user/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
// 수정안: Authorization 헤더가 있으면 무조건 JWT 검사로 덮어쓴다
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 헤더가 없을 때만 기존 인증 유지
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 토큰 확인
        log.info("[JwtAuth] path={}, rawAuth='{}'", request.getRequestURI(), authHeader);
        log.info("[JwtAuth] token(prefix10)={}, len={}",
                token.substring(0, Math.min(10, token.length())), token.length());

        String type = jwtProvider.getTokenTypeStrict(token);
        if (!"access".equals(type)) {
            log.info("[JwtAuth] non-access token on path {}: {}", request.getRequestURI(), type);
            filterChain.doFilter(request, response);
            return; // refresh 토큰이면 인증세팅 안하고 패스
        }


        try {
            Long userId = jwtProvider.getUserIdFromAccessOrNull(token);
            String kakaoId = jwtProvider.getKakaoIdFromAccessOrNull(token);

            // ===== dev 화이트리스트 우회 여부 판단 =====
            boolean devBypass = devWhitelistProperties != null
                    && devWhitelistProperties.isEnabled()
                    && kakaoId != null
                    && devWhitelistProperties.allows(kakaoId);
            log.info("[JwtAuth] type={}, uid={}, kid={}", type, userId, kakaoId);
            log.info("[JwtAuth] devBypass={}, whitelistEnabled={}",
                    devBypass, (devWhitelistProperties != null && devWhitelistProperties.isEnabled()));

            User user = null;
            boolean termsAgreed = false;

//            우회면 DB조회/약관 건너뜀
            if (devBypass) {
                // 유저가 아직 없을 수도 있으니, 그대로 통과할 수 있는 Principal을 만들어
                UserPrincipal principal = UserPrincipal.devBypass(userId, kakaoId); // <- 여기서 내부적으로 terms=true, onboarding=true 보장
                var authorities = new ArrayList<SimpleGrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_DEV"));

                log.info("[JwtAuth] result principal: onboarded={}, termsAgreed={}, roles={}",
                        principal.isOnboardingNeeded(), principal.isTermsAgreed(), authorities);

                var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
                return;
            }

//            정상 경로: DB 조회 + 약관 체크
            try {
                if (userId != null) {
                    user = userService.getUserById(userId);
                    termsAgreed = (user != null) && userTermsService.hasAgreedRequired(userId);
                }
            } catch (Exception svcEx) {
                log.warn("[JwtAuth] user/terms lookup failed: {}", svcEx.toString());
            }

            // 5) Principal 구성: uid 없을 때도 kakaoId로 폴백 가능하도록
            UserPrincipal principal = UserPrincipal.from(user, termsAgreed, kakaoId);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            log.info("[JwtAuth] result principal: onboarded={}, termsAgreed={}, roles={}",
                    principal.isOnboardingNeeded(), principal.isTermsAgreed(), authorities);

            var auth =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6) SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (io.jsonwebtoken.JwtException jwtEx) {
            // JWT 문제면 인증 제거
            log.info("[JwtAuth] jwt error: {}", jwtEx.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.info("[JwtAuth] error: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
