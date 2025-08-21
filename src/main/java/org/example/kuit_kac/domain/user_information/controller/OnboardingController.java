package org.example.kuit_kac.domain.user_information.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.PrePrincipal;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.dto.OnboardingRequest;
import org.example.kuit_kac.domain.user_information.dto.OnboardingResponse;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.example.kuit_kac.global.util.dev.DevAuthService;
import org.example.kuit_kac.global.util.dev.DevAutofillProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final DevAuthService devAuthService;

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
            - 액세스토큰
            - 리프레쉬토큰
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
            @AuthenticationPrincipal Object principal,
            @RequestBody @Valid OnboardingRequest req
    ) {
        boolean isPreStage = false;

        // 1) kid 꺼내기
        String kid = null;
        Long uid  = null;
        if (principal instanceof PrePrincipal pre) {
            kid = pre.getKakaoId();
            isPreStage = true;
        } else if (principal instanceof UserPrincipal up) {
            kid = up.getKakaoId();
            uid = up.getUserId();
            if (uid == null) {
                isPreStage = true;
            }
        }

//        final String kid = resolveKid(principal, bearer, httpRequest);
        if (kid == null || kid.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        // DEV 판단: dev 필터가 넣는 가짜 uid < 0 를 기준
        boolean isDev = (principal instanceof UserPrincipal up2)
                && up2.getUserId() != null && up2.getUserId() < 0L;
        // 3) 온보딩 처리 - 신규 유저면 생성하고, 기존 유저가 온보딩 전이면 채워줌
        OnboardingResponse body = onboardingService.createUserWithOnboarding(
                kid, req,
                autofill.isEnabled() && isDev  // DEV일 때만 자동채움
        );

        // 4) PRE → USER 토큰 교체가 필요한 경우: 응답 헤더에 토큰 얹기 (바디는 변경 없음)
        HttpHeaders headers = new HttpHeaders();
        if (isPreStage) {
            // 방금 생성(또는 업데이트)된 유저의 id 확보
            Long userId = body.getUserId(); // 서비스 반환값에 포함됨

            String newAccess  = jwtProvider.generateUserAccessToken(userId, kid);
            String newRefresh = jwtProvider.generateRefreshToken(userId);

            headers.add("X-Access-Token", newAccess);
            headers.add("X-Refresh-Token", newRefresh);
            headers.add("X-Token-Expires-In", String.valueOf(seconds(jwtProvider))); 
        }

        return ResponseEntity.ok(body);
    }

    private long seconds(JwtProvider jwt) {
        // JwtProvider 내부에 getAccessTtlMs() 게터가 없다면 적절히 노출하거나 상수/프로퍼티로 전달
        try {
            var f = JwtProvider.class.getDeclaredField("accessTtlMs");
            f.setAccessible(true);
            return ((Number) f.get(jwt)).longValue() / 1000L;
        } catch (Exception ignore) {
            return 900L; // fallback 15분
        }
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