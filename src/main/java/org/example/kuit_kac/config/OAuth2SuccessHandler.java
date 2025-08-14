package org.example.kuit_kac.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.domain.user_information.service.UserInfoService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AuthLoginSuccessProperties props;
    private final AuthOnboardingProperties onboardingProperties;
    private final UserInfoService userInfoService;

    // 빈 페이지에 토큰 출력하는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("[OAuth2Success] mode={}, deepLink='{}', isDeepLinkBranch={}",
                props.getMode(), props.getDeepLink(), props.isDeepLink());

        // 1) 사용자 정보
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attrs = oAuth2User.getAttributes();

        Long userId = null;
        Object uidAttr = attrs.get("userId");
        if (uidAttr instanceof Number n) userId = n.longValue();

        String kakaoId = oAuth2User.getName();

        // 2) 토큰 생성
        String access = jwtProvider.generateAccessToken(userId, kakaoId);
        String refresh = jwtProvider.generateRefreshToken(userId);

        // 만료(초) / state
        long expiresIn = props.getAccessTtlSeconds();
        String state = request.getParameter("state"); // 있으면 전달

        // 3) 온보딩 필요 여부(TODO: 미구현이면 yml 기본값 true, 구현후 실제 DB검사)
        boolean onboardingRequired =
                onboardingProperties.isRequire() && userId != null && userInfoService.isOnboardingRequired(userId);

//        // TODO: 서버토큰 JSON 활성화 코드. 지워야함!
//        writeJson(response, access, refresh, expiresIn, state, onboardingRequired);

        if (!props.isDeepLink()) {
            writeJson(response, access, refresh, expiresIn, state, onboardingRequired);
            return;
        }


        // === DEEPLINK 모드: 무조건 리다이렉트 (JSON 폴백 금지) ===
        String target = props.getDeepLink();
        if (target == null || target.isBlank()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing deep-link config");
            return;
        }

        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(target)
                .queryParam(props.getAccessParam(), access)
                .queryParam(props.getRefreshParam(), refresh)
                .queryParam(props.getExpiresParam(), expiresIn)
                .queryParam(props.getOnboardingParam(), onboardingRequired);
        if (state != null && !state.isBlank()) {
            b.queryParam(props.getStateParam(), state);
        }

        String deep = b.build().encode().toUriString();

        // 302 Location
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", deep);
        response.setContentLength(0); // 바디 금지
    }

    private static void writeJson(HttpServletResponse response,
                                  String access, String refresh, long expiresIn,
                                  String state, boolean onboardingRequired) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        StringBuilder sb = new StringBuilder()
                .append("{\"accessToken\":\"").append(access).append("\",")
                .append("\"refreshToken\":\"").append(refresh).append("\",")
                .append("\"expiresIn\":").append(expiresIn).append(",")
                .append("\"onboardingRequired\":")
                .append(onboardingRequired);
        if (state != null) sb.append(",\"state\":\"").append(state).append("\"");
        sb.append("}");
        response.getWriter().write(sb.toString());
    }

}
