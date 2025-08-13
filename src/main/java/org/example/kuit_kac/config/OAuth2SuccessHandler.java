package org.example.kuit_kac.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AuthLoginSuccessProperties props; // @ConfigurationProperties 바인딩

    // 빈 페이지에 토큰 출력하는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // OAuth2User 객체에서 사용자 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long userId = (Long) oAuth2User.getAttributes().get("userId");
        String kakaoId = oAuth2User.getName();

        String access = jwtProvider.generateAccessToken(userId, kakaoId);
        String refresh = jwtProvider.generateRefreshToken(userId);

        if (props.isDeepLink()) {
            String a = URLEncoder.encode(access, StandardCharsets.UTF_8);
            String r = URLEncoder.encode(refresh, StandardCharsets.UTF_8);

            // fragment 사용 권장(서버/프록시 로그에 덜 남도록)
            String target = props.getDeepLink();
            if (target == null || target.isBlank()) {
                // 설정 누락 시 안전하게 JSON으로 폴백
                writeJson(response, access, refresh);
                return;
            }
            String deep = target + "#" + props.getAccessParam() + "=" + a + "&" + props.getRefreshParam() + "=" + r;
            response.sendRedirect(deep);
            return;
        }

        // 개발용: JSON 응답
        writeJson(response, access, refresh);
    }

    private static void writeJson(HttpServletResponse response, String access, String refresh) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"accessToken\":\"" + access + "\"\n,\"refreshToken\":\"" + refresh + "\"}");
        response.getWriter().flush();
    }
}
