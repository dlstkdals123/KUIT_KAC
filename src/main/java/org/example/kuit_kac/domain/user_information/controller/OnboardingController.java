package org.example.kuit_kac.domain.user_information.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.dto.OnboardingRequest;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding")
@Schema(name = "온보딩", description = "회원 온보딩 관련 API")
public class OnboardingController {
    private final OnboardingService onboardingService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Operation(
            summary = "온보딩 등록",
            description = """
                    회원 가입 시 필수 정보(온보딩)를 등록합니다.<br>
                    - `Authorization` 헤더의 Access 토큰 또는 AuthenticationPrincipal에서 카카오 ID를 추출하여 처리합니다.<br>
                    - 이미 존재하는 회원이 온보딩 완료 상태라면 에러를 반환합니다.
                    """
    )
    @Parameter(
            name = "Authorization",
            description = "Bearer {Access 토큰}",
            in = ParameterIn.HEADER,
            required = true,
            example = "Bearer eyJhbGciOi..."
    )
    @ApiResponse(
            responseCode = "200",
            description = "온보딩 완료",
            content = @Content(schema = @Schema(example = "{\"userId\": 3}"))
    )
    @ApiResponse(
            responseCode = "401",
            description = "토큰에 kid 없음 / 인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "요청 데이터 유효성 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping
    public ResponseEntity<Map<String, Object>> onboarding(

            @AuthenticationPrincipal UserPrincipal principal,
            @RequestHeader(value = "Authorization", required = false) String bearer,
            @RequestBody @Valid OnboardingRequest req) {

        String kid = null;

        // Principal에서 시도
        if (principal != null && principal.getKakaoId() != null && !principal.getKakaoId().isBlank()) {
            kid = principal.getKakaoId();
        }

        // 헤더 access 토큰에서 추출
        if (kid == null && bearer != null && bearer.startsWith("Bearer ")) {
            String access = bearer.substring(7);
            kid = jwtProvider.getKakaoIdOrNullFromToken(access);

            // kid가 여전히 없고 uid가 있으면 DB로 역조회
            if (kid == null) {
                Long uid = jwtProvider.getUserIdOrNullFromToken(access);
                if (uid != null) {
                    kid = userService.getUserById(uid).getKakaoId(); // 온보딩 완료 사용자라면 존재
                }
            }
        }

        if (kid == null || kid.isBlank()) {
            // 토큰에 kid가 진짜로 없거나, uid도 없어 역조회 실패
            return ResponseEntity.status(401).body(Map.of("error", "missing kid in token"));
        }

        Long userId = onboardingService.createUserWithOnboarding(kid, req);
        return ResponseEntity.ok(Map.of("userId", userId));
    }
}
