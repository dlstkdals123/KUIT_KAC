package org.example.kuit_kac.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AuthLoginSuccessProperties props;
    private final AuthOnboardingProperties onboardingProperties;
    private final OnboardingService onboardingService;

    // 빈 페이지에 토큰 출력하는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("[OAuth2Success] mode={}, deepLink='{}', isDeepLinkBranch={}",
                props.getMode(), props.getDeepLink(), props.isDeepLink());

        // 1) 사용자 정보
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attrs = oAuth2User.getAttributes();

        // kakaoId: attributes에 "kakaoId"가 있으면 사용, 없으면 카카오 원본 "id", 둘 다 없으면 getName()
        String kakaoId = null;
        Object kidAttr = attrs.get("kakaoId");
        if (kidAttr == null) kidAttr = attrs.get("id");   // 카카오 원본
        if (kidAttr != null) kakaoId = String.valueOf(kidAttr);
        if (kakaoId == null || kakaoId.isBlank()) {
            // nameAttributeKey를 "kakaoId"로 설정했다면 getName()이 kakaoId일 수 있음
            String fallback = oAuth2User.getName();
            if (fallback != null && !fallback.isBlank()) {
                kakaoId = fallback;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "kakaoId not found");
                return;
            }
        }

        Long userId = null;
        Object uidAttr = attrs.get("userId");
        if (uidAttr instanceof Number n) {
            userId = n.longValue();
        } else if (uidAttr instanceof String s && !s.isBlank()) {
            try {
                userId = Long.parseLong(s);
            } catch (NumberFormatException ignored) {
            }
        }

        // 2) 토큰 생성 (클레임 Map으로 유연하게)
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("sub", "access");
        accessClaims.put("kid", kakaoId); // 프론트가 디코딩해서 온보딩 요청에 실어보내면 됨
        if (userId != null) accessClaims.put("uid", userId);

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("sub", "refresh");
        refreshClaims.put("kid", kakaoId);
        if (userId != null) refreshClaims.put("uid", userId);

        String access = jwtProvider.generateAccessToken(userId, kakaoId); // kakaoId를 access에만 실어줌
        String refresh = jwtProvider.generateRefreshToken(userId);
        long expiresIn = props.getAccessTtlSeconds();
        String state = request.getParameter("state"); // 있으면 전달


        // 3) 온보딩 필요 여부
        boolean onboardingRequired;
        if (!onboardingProperties.isRequire()) {
            onboardingRequired = false;
        } else if (userId != null) {
            onboardingRequired = onboardingService.isOnboardingRequired(userId);
        } else {
            onboardingRequired = true;
        }

//        // TODO: 서버토큰 JSON 활성화 코드 넣는 자리

        String target = props.getDeepLink();

        String deep = UriComponentsBuilder.fromUriString(target)
                .queryParam(props.getAccessParam(), access)
                .queryParam(props.getRefreshParam(), refresh)
                .queryParam(props.getExpiresParam(), expiresIn)
                .queryParam(props.getOnboardingParam(), onboardingRequired)
                .queryParam(props.getStateParam(), (state == null ? "" : state))
                .build().encode().toUriString();

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", deep);
        response.setContentLength(0);
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
