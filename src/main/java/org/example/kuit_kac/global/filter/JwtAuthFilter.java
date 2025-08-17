package org.example.kuit_kac.global.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserService userService; // 최신값 필요하면 조회해서 Principal 새로 만듦
    private final UserTermsService userTermsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String p = request.getServletPath();
        return p.startsWith("/oauth2/") // 로그인 시작
                || p.startsWith("/login/oauth2/") // 콜백
                || p.startsWith("/swagger-ui/")
                || p.startsWith("/v3/api-docs/")
                || p.equals("/") || p.startsWith("/health") || p.startsWith("/actuator")
                // TODO: 배포 전 막아야 함
                || p.startsWith("/dev-tools/") || p.startsWith("/dev-auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 이미 인증된 상태면 다음 필터로
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 요청 헤더에서 Authorization 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 토큰 확인
        log.info("[JwtAuth] path={}, rawAuth='{}'", request.getRequestURI(), authHeader);
        log.info("[JwtAuth] token(prefix10)={}, len={}",
                token.substring(0, Math.min(10, token.length())), token.length());

        String type = jwtProvider.getTokenType(token);
        if (!"access".equals(type)) {
            log.info("[JwtAuth] non-access token on path {}: {}", request.getRequestURI(), type);
            filterChain.doFilter(request, response);
            return; // refresh 토큰이면 인증세팅 안하고 패스
        }


        try {
            Claims c;
            c = jwtProvider.parseAccess(token); // accessKey로만 검증
            if (!"access".equals(c.getSubject())) { // 더블체크
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = jwtProvider.getUserIdOrNullFromToken(token);
            String kakaoId = jwtProvider.getKakaoIdOrNullFromToken(token);

            User user = null;
            boolean termsAgreed = false;
            try {
                if (userId != null) {
                    user = userService.getUserById(userId);
                    termsAgreed = (user != null) && userTermsService.hasAgreedRequired(userId);
                }
            } catch (Exception svcEx) {
                // 서비스 오류는 인증을 깨지 않고, 로그만 남기고 계속 진행
                log.warn("[JwtAuth] user/terms lookup failed: {}", svcEx.toString());
            }
            // 5) Principal 구성: uid 없을 때도 kakaoId로 폴백 가능하도록
            UserPrincipal principal = UserPrincipal.from(user, termsAgreed, kakaoId);

            var auth =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6) SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (io.jsonwebtoken.JwtException jwtEx) {
            // JWT 문제일 때만 인증 제거 → 401로 떨어지게 함
            SecurityContextHolder.clearContext();
        }
        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
