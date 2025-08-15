package org.example.kuit_kac.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(name = "약관 응답 DTO", description = "사용자별 약관 동의 상세")
public class TermAgreementResponse {
    @Schema(description = "사용자 ID", example = "3")
    private Long userId;

    @Schema(description = "약관 코드", example = "SERVICE_TOS")
    private TermCode code;

    @Schema(description = "약관 버전", example = "v1.0.0")
    private String version;

    @Schema(description = "동의 여부", example = "true")
    private boolean agreed;

    @Schema(description = "동의한 시각")
    private LocalDateTime agreedAt;

    @Schema(description = "철회한 시각", nullable = true)
    private LocalDateTime withdrawnAt;
}
