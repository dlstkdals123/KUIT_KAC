package org.example.kuit_kac.domain.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.oauth.dto.TokenResponse;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.StringTokenizer;

@RestController
@RequiredArgsConstructor
@Schema(name = "인증", description = "로그인/토큰 관련 API")
public class AuthController {
    private final JwtProvider jwtProvider;

    @Operation(summary = "토큰 재발급",
            description = "Refresh 토큰으로 새로운 Access/Refresh 토큰을 발급합니다. (Authorization 헤더에 Refresh 토큰 필요)")
    @Parameters({
            @Parameter(
                    name = "Authorization",
                    description = "Bearer {Refresh 토큰}",
                    in = ParameterIn.HEADER,
                    required = true,
                    example = "Bearer eyJhbGciOi..."
            )
    })
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "401", description = "만료/잘못된 리프레시 토큰",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {}
            )
    )
    @PostMapping("/auth/refresh")
    // refresh 토큰을 받아서 새 토큰을 발급
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization") String bearer) {
        String token = (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
        if (token == null || !jwtProvider.validateToken(token) || !"refresh".equals(jwtProvider.getTokenType(token))) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        Long uid = jwtProvider.getUserIdFromToken(token);
        String newAccess = jwtProvider.generateAccessToken(uid, null);
        String newRefresh = jwtProvider.generateRefreshToken(uid);
        return ResponseEntity.ok(new TokenResponse(newAccess, newRefresh));
    }
}