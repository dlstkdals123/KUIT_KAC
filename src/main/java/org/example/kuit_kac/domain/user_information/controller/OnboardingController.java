package org.example.kuit_kac.domain.user_information.controller;

import io.swagger.v3.oas.annotations.media.Schema;
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