package org.example.kuit_kac.domain.user_information.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.dto.OnboardingRequest;
import org.example.kuit_kac.domain.user_information.dto.OnboardingResponse;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.example.kuit_kac.global.util.dev.DevAutofillProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding")
@Schema(name = "온보딩", description = "회원 온보딩 관련 API")
@Tag(name = "온보딩", description = "회원 온보딩 관련 API")
public class OnboardingController {
    private final OnboardingService onboardingService;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final DevAutofillProperties autofill; // ← 주입

    @Operation(
            summary = "회원 온보딩 API 🚀",
            description = """
            신규 사용자의 **온보딩 정보를 등록**하고,
            BMR(기초대사량) 및 일일 감량 목표 칼로리를 반환합니다.  

            ✅ 요청 헤더:
            - Authorization: Bearer 액세스 토큰 (선택)  
            - X-KID: 디버그용 사용자 식별자 (선택)  

            ✅ 요청 본문:
            - 닉네임, 성별, 나이, 키, 현재/목표 체중 등 사용자 기초 정보  
            - 다이어트 경험, 식욕 유형, 외식 습관, 다이어트 속도 등 생활 패턴 정보  
            - 약관 동의 정보(선택)  

            ✅ 응답 본문:
            - 생성된 유저 ID  
            - 계산된 BMR 값  
            - 일일 감량 목표 칼로리  
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "온보딩 성공 🎉",
                            content = @Content(schema = @Schema(implementation = OnboardingResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패 ❌ (토큰/식별자 누락)"),
                    @ApiResponse(responseCode = "500", description = "서버 오류 💥")
            }
    )
    @PostMapping
    public ResponseEntity<OnboardingResponse> onboarding(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestHeader(value = "Authorization", required = false) String bearer,
            @RequestBody @Valid OnboardingRequest req,
            jakarta.servlet.http.HttpServletRequest httpRequest
    ) {
        final String kid = resolveKid(principal, bearer, httpRequest);
        if (kid == null || kid.isBlank()) {
            return ResponseEntity.status(401).build();
        }


        // DEV 판단: dev 필터가 넣는 가짜 uid < 0 를 기준
        boolean isDev = principal != null && principal.getUserId() != null && principal.getUserId() < 0L;

        OnboardingResponse response = onboardingService.createUserWithOnboarding(
                kid, req,
                autofill.isEnabled() && isDev
        );
        return ResponseEntity.ok(response);
    }

    private String resolveKid(UserPrincipal principal, String bearer, jakarta.servlet.http.HttpServletRequest req) {
        if (principal != null && principal.getKakaoId() != null && !principal.getKakaoId().isBlank()) {
            return principal.getKakaoId();
        }
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String t = bearer.substring(7);
            try {
                String kid = jwtProvider.getKakaoIdFromAccessOrNull(t);
                if (kid != null && !kid.isBlank()) return kid;
                Long uid = jwtProvider.getUserIdFromAccessOrNull(t);
                if (uid != null && uid >= 0L) {
                    try { return userService.getUserById(uid).getKakaoId(); } catch (Exception ignore) {}
                }
            } catch (Exception ignore) {}
        }
        String headerKid = req.getHeader("X-KID");
        if (headerKid != null && !headerKid.isBlank()) return headerKid;

        String qpKid = req.getParameter("kid"); // 로컬 디버그
        if (qpKid != null && !qpKid.isBlank()) return qpKid;

        return null;
    }
}