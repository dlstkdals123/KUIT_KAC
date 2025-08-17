package org.example.kuit_kac.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(name = "에러 응답")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "AUTH_001")
    private final String code;

    @Schema(description = "에러 메시지", example = "인증이 필요합니다.")
    private final String message;

    @Schema(description = "발생 시각", example = "2025-08-15T20:30:00")
    private final LocalDateTime timestamp;
}
