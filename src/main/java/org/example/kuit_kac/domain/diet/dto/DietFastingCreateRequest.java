package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "단식 생성 요청 DTO")
public record DietFastingCreateRequest(
    @Schema(description = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유저 ID는 필수입니다.")
    Long userId,

    @Schema(description = "식단 날짜", example = "2025-08-09", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "식단 날짜는 필수입니다.")
    LocalDate dietDate,

    @Schema(description = "식단 종류", example = "아침", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "식단 종류는 필수입니다.")
    String dietType
) {} 