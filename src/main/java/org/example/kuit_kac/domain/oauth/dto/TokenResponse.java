package org.example.kuit_kac.domain.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "토큰 응답 DTO")
public class TokenResponse {
    @Schema(description = "새 access 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String accessToken;

    @Schema(description = "새 refresh 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String refreshToken;
}
