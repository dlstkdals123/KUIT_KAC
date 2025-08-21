package org.example.kuit_kac.global.util.dev;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
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
    private final DevAuthService devAuthService;

    // 예: GET /dev-auth/mint?uid=3&kid=4384440657
    @Hidden
    @GetMapping("/mint")
    public Map<String, Object> mint(@RequestParam(required = false) Long uid,
                                    @RequestParam(required = true) String kid) {
        String access = null;
        String refresh = null;
        if (uid == null) {
            access = jwtProvider.generatePreAccessToken(kid); // kid는 access에 실림
            log.info("pre 토큰 발급: " + access);
            return Map.of("accessToken", access);
        } else {
            if (kid == null || kid.isBlank()) {
                return Map.of("error","kid required");
            }
            Map<String, Object> mintUserStrict = devAuthService.mintUserStrict(uid, kid);

            access = mintUserStrict.get("access").toString();
            refresh = mintUserStrict.get("refresh").toString();
            log.info("user 토큰 발급: " + access);
            return Map.of("accessToken", access, "refreshToken", refresh);
        }
    }

    @Hidden
    // kid만으로 발급(온보딩 전 시나리오) — uid는 null
    @GetMapping("/mint-anon")
    public Map<String, String> mintAnon(@RequestParam String kid) {
        String access = jwtProvider.generateUserAccessToken(null, kid);
        String refresh = jwtProvider.generateRefreshToken(null);
        return Map.of("accessToken", access, "refreshToken", refresh);
    }

}
