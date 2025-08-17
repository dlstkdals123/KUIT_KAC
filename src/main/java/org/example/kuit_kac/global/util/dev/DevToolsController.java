package org.example.kuit_kac.global.util.dev;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dev-tools")
@RequiredArgsConstructor
@Profile("local") // 로컬 환경에서만 동작
public class DevToolsController {

    private final DevResetService devResetService;

    @Operation(
            summary = "테스트용: 특정 유저 데이터 초기화",
            description = "테스트 목적의 엔드포인트. 지정한 userId의 약관 동의 및 온보딩 데이터를 모두 삭제합니다. (실 서비스에서 사용 금지)",
            parameters = {
                    @Parameter(name = "userId", description = "초기화할 사용자 ID", in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "초기화 완료"),
                    @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
            }
    )
    @DeleteMapping("/reset-user/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable long userId) {
        devResetService.softResetUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Authorization 헤더 raw/분리 확인용
    @GetMapping("/echo-auth")
    public Map<String, Object> echoAuth(@RequestHeader(value = "Authorization", required = false) String auth) {
        String token = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
        return Map.of("raw", auth, "hasBearer", auth != null && auth.startsWith("Bearer "), "tokenPrefix10",
                token == null ? null : token.substring(0, Math.min(10, token.length())));
    }

    // JWT header/payload 디코드 (서명 검증 X, 개발용)
    @GetMapping("/decode-token")
    public Map<String, Object> decodeToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Map.of("error", "Invalid JWT format");
        }

        // Base64 디코딩
        String headerJson = new String(java.util.Base64.getUrlDecoder().decode(parts[0]));
        String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));

        return Map.of(
                "header", headerJson,
                "payload", payloadJson
        );
    }
}