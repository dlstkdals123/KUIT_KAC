package org.example.kuit_kac.global.filter;

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
                || p.equals("/") || p.startsWith("/health") || p.startsWith("/actuator");
//                || p.startsWith("/reset-user/");
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
            Long userId = jwtProvider.getUserIdOrNullFromToken(token);
            String kakaoId = jwtProvider.getKakaoIdOrNullFromToken(token);

            User user = (userId != null) ? userService.getUserById(userId) : null;
            // TODO: 약관 동의 여부 계산 로직 연결
            boolean termsAgreed = (user != null) && userTermsService.hasAgreedRequired(userId);

            // 5) Principal 구성: uid 없을 때도 kakaoId로 폴백 가능하도록
            UserPrincipal principal = UserPrincipal.from(user, termsAgreed, kakaoId);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6) SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
