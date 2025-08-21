package org.example.kuit_kac.global.util.dev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dev-auth")
@RequiredArgsConstructor
@Slf4j
public class DevAuthController {
    private final JwtProvider jwtProvider;

    // 예: GET /dev-auth/mint?uid=3&kid=4384440657
    @GetMapping("/mint")
    public Map<String, String> mint(@RequestParam(required = false) Long uid,
                                    @RequestParam(required = true) String kid) {
        String access = null;
        String refresh = null;
        if (uid == null) {
            access = jwtProvider.generatePreAccessToken(kid); // kid는 access에 실림
            log.info("pre 토큰 발급: " + access);
            return Map.of("accessToken", access);
        } else {
            access = jwtProvider.generateUserAccessToken(uid, kid);
            refresh = jwtProvider.generateRefreshToken(uid);
            log.info("user 토큰 발급: " + access);
            return Map.of("accessToken", access, "refreshToken", refresh);
        }
    }

    // kid만으로 발급(온보딩 전 시나리오) — uid는 null
    @GetMapping("/mint-anon")
    public Map<String, String> mintAnon(@RequestParam String kid) {
        String access = jwtProvider.generateUserAccessToken(null, kid);
        String refresh = jwtProvider.generateRefreshToken(null);
        return Map.of("accessToken", access, "refreshToken", refresh);
    }

}
