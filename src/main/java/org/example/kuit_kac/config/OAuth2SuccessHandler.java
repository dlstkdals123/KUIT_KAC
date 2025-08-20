
package org.example.kuit_kac.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.example.kuit_kac.domain.user_information.service.OnboardingStatusService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.example.kuit_kac.global.util.dev.DevWhitelistProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final AuthLoginSuccessProperties props;
    private final AuthOnboardingProperties onboardingProperties;
    private final OnboardingStatusService onboardingStatusService;
    private final DevWhitelistProperties devWhitelistProperties;

    @Value("${debug.kid-auth.secret}")
    private String kidAuthSecret;
    @Autowired
    private org.springframework.core.env.Environment env;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("[OAuth2Success] mode={}, deepLink='{}', isDeepLinkBranch={}", props.getMode(), props.getDeepLink(), props.isDeepLink());
        // 1) 사용자 정보
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attrs = oAuth2User.getAttributes();
        // kakaoId: attributes에 "kakaoId"가 있으면 사용, 없으면 카카오 원본 "id", 둘 다 없으면 getName()
        String kakaoId = null;
        Object kidAttr = attrs.get("kakaoId");
        if (kidAttr == null)
            kidAttr = attrs.get("id"); // 카카오 원본
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
        accessClaims.put("kid", kakaoId);
        // 프론트가 디코딩해서 온보딩 요청에 실어보내면 됨
        if (userId != null) accessClaims.put("uid", userId);
        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("sub", "refresh");
        refreshClaims.put("kid", kakaoId);
        if (userId != null) refreshClaims.put("uid", userId);
        String access = jwtProvider.generateAccessToken(userId, kakaoId); // kakaoId를 access에만 실어줌
        String refresh = jwtProvider.generateRefreshToken(userId);
        long expiresIn = props.getAccessTtlSeconds();
        // 3) 온보딩 필요 여부
        boolean onboardingRequired;
        if (!onboardingProperties.isRequire()) {
            onboardingRequired = false;
        } else if (userId != null) {
            onboardingRequired = onboardingStatusService.isOnboardingRequired(userId);
        } else {
            onboardingRequired = true;
        }

        // ✅ DEV 화이트리스트면 무조건 우회
        boolean devBypass = devWhitelistProperties != null
                && devWhitelistProperties.isEnabled()
                && devWhitelistProperties.allows(kakaoId);
        if (devBypass) {
            onboardingRequired = false;
            log.info("[OAuth2Success] DEV bypass: force onboardingRequired=false for kid={}", kakaoId);
        }

        log.info("[OAuth2Success] onboarding.require(yml)={} -> final.onboardingRequired={}",
                onboardingProperties.isRequire(), onboardingRequired);

        // // TODO: 서버토큰 JSON 활성화 코드 넣는 자리

        // 딥링크 파라미터
        String target = props.getDeepLink();
        boolean useDeepLink = "DEEPLINK".equalsIgnoreCase(props.getMode().toString());
        if (!useDeepLink || target == null || target.isBlank()) {
            log.error("[OAuth2Success] deep-link not configured. Falling back to JSON response.");
// writeJson
            return;
        }
        long ts = Instant.now().getEpochSecond();
        String sig = hmacSha256(kidAuthSecret, kakaoId + "|" + ts);
        String state = request.getParameter("state");


        String deep = UriComponentsBuilder
                .fromUriString(target)
//                .queryParam(props.getAccessParam(), access)
//                .queryParam(props.getRefreshParam(), refresh)
//                .queryParam(props.getExpiresParam(), expiresIn)
                .queryParam("kid", kakaoId)
                .queryParam("ts", ts)
                .queryParam("sig", sig)
//                .queryParam(props.getOnboardingRequired(), onboardingRequired)
//                .queryParam(props.getStateParam(), (state == null ? "" : state))
                .build().encode() // 인코딩 보존
                .toUriString();

        // 실제 딥링크
            // 로컬/개발에서만 전체 URL 찍기 (운영 금지)
            log.info("[OAuth2Success] deepLinkUrl={}", deep); // 로컬/DEV + DEBUG일 때만 전체 출력


        // 민감값 마스킹된 파라미터 로그 (운영에서도 안전)
        log.info("[OAuth2Success] deeplink params: {}={}  {}={}  kid={}  ts={}  {}={}  {}={}  state='{}'",
                props.getAccessParam(), preview(access),
                props.getRefreshParam(), preview(refresh),
                kakaoId,
                ts,
                props.getExpiresParam(), expiresIn,
                props.getOnboardingRequired(), onboardingRequired, // ← 네 프로퍼티 이름에 맞춰 유지
                state);

        log.info("[OAuth2Success] Redirecting to app deeplink. kid={}, ts={}, onboardingRequired={}", kakaoId, ts, onboardingRequired);
        log.info("[OAuth2Success] final.onboardingRequired={}, userId={}", onboardingRequired, userId);

        // 302 리다이렉트
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", deep);
        response.setContentLength(0);
    }

    // 민감값 미리보기 (앞 10글자 + 길이)
    private static String preview(String s) {
        if (s == null) return "null";
        int n = Math.min(10, s.length());
        return s.substring(0, n) + "...(len=" + s.length() + ")";
    }

    // HMAC 유틸
    private String hmacSha256(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeJson(HttpServletResponse response, String access, String refresh, long expiresIn, String state, boolean onboardingRequired, String kakaoId) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        StringBuilder sb = new StringBuilder()
                .append("{\"accessToken\":\"")
                .append(access).append("\",")
                .append("\"refreshToken\":\"")
                .append(refresh).append("\",")
                .append("\"expiresIn\":")
                .append(expiresIn).append(",")
                .append("\"onboardingRequired\":")
                .append(onboardingRequired)
                .append("\"kakaoId\":\"")
                .append(kakaoId).append("\"");
        if (state != null)
            sb.append(",\"state\":\"")
                    .append(state).append("\"");
        sb.append("}");
        response.getWriter().write(sb.toString());
    }
}
