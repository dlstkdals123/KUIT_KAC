package org.example.kuit_kac.domain.user.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.dto.UserResponse;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "사용자 관리", description = "사용자 정보 조회, 생성, 수정, 삭제 등 사용자 관련 모든 기능을 제공합니다.")
public class UserController {

    private final UserService userService;

    // 내 정보 조회 전용 엔드포인트
    @Operation(
            summary = "내 정보 조회",
            description = "인증된 사용자의 프로필을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(hidden = true)
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(
            responseCode = "401", description = "인증 필요",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {}
            ))
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserPrincipal p) {
        if (p == null) {
            // 인증 안 된 경우
            return ResponseEntity.status(401)
                    .body(UserResponse.builder()
                            .termsAgreed(false)
                            .onboardingNeeded(true)
                            .build());
        }

        Long userId = p.getUserId();

        // 1) uid가 있는 경우 (온보딩 완료 사용자)
        if (userId != null) {
            User u = userService.getUserById(userId);
            return ResponseEntity.ok(
                    UserResponse.from(u, p.isTermsAgreed(), p.isOnboardingNeeded())
            );
        }

        // 2) uid가 없으면 kid(kakaoId) 폴백
        String kakaoId = p.getKakaoId();
        if (kakaoId == null || kakaoId.isBlank()) {
            // 토큰에 kakaoId도 없으면 잘못된 토큰
            return ResponseEntity.status(401)
                    .body(UserResponse.builder()
                            .termsAgreed(false)
                            .onboardingNeeded(true)
                            .build());
        }

        // kid로 조회 시 DB에 있으면 UserResponse 반환
        return userService.findByKakaoId(kakaoId)
                .map(u -> ResponseEntity.ok(
                        UserResponse.from(u, p.isTermsAgreed(), p.isOnboardingNeeded())
                ))
                .orElseGet(() -> ResponseEntity.status(409) // 온보딩 필요
                        .body(UserResponse.builder()
                                .termsAgreed(p.isTermsAgreed())
                                .onboardingNeeded(true)
                                .build()));
    }


    @Hidden // 프론트에서 안 쓰므로 숨김
// 다른 user 정보 접근 못하는지 확인하는 API
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        if (!principal.getUserId().equals(id)) {
            throw new CustomException(ErrorCode.AUTH_FORBIDDEN);
        }
        // 내 것일 때만 통과
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponse.from(user, principal.isTermsAgreed(), principal.isOnboardingNeeded()));
    }
}
