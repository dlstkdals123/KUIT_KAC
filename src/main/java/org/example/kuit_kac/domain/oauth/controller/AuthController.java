package org.example.kuit_kac.domain.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.domain.oauth.dto.TokenResponse;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@Schema(name = "인증", description = "로그인/토큰 관련 API")
public class AuthController {
    private final JwtProvider jwtProvider;

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh 토큰을 사용하여 새로운 Access/Refresh 토큰을 발급합니다."
    )
    @Parameter(
            name = "Authorization",
            description = "Bearer {Refresh 토큰}",
            in = ParameterIn.HEADER,
            required = true,
            example = "Bearer eyJhbGciOi..."
    )
    @ApiResponse(
            responseCode = "200",
            description = "재발급 성공",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "만료/잘못된 Refresh 토큰",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping("/refresh")
    // refresh 토큰을 받아서 새 토큰을 발급
    public ResponseEntity<TokenResponse> refresh(@RequestHeader(value = "Authorization", required = false) String bearer) {


        log.info("[/auth/refresh] bearer={}",
                bearer != null ? bearer.substring(0, Math.min(30, bearer.length())) : null);

        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        String token = bearer.substring(7);
        String type = jwtProvider.getTokenTypeStrict(token);
        if (!jwtProvider.validateToken(token) || !"refresh".equals(type)) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        Long   uid = jwtProvider.getUserIdFromAccessOrNull(token);   // ← 예외 대신 null 가능
        String kid = jwtProvider.getKakaoIdFromAccessOrNull(token);  // ← 유지 권장

        String newAccess  = jwtProvider.generateAccessToken(uid, kid);
        String newRefresh = jwtProvider.generateRefreshToken(uid); // 회전 정책이면 유지

        return ResponseEntity.ok(new TokenResponse(newAccess, newRefresh));
    }
}