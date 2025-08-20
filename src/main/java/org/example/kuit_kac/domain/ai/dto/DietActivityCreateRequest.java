package org.example.kuit_kac.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "AI 식단 생성 요청 DTO")
public record DietActivityCreateRequest(
    @Schema(description = "외식/술자리 날짜 (형식: YYYY-MM-DD)", example = "2025-08-09", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "외식/술자리 날짜는 필수입니다.")
    LocalDate dietDate,

    @Schema(description = "식단 종류 (아침, 점심, 저녁)", example = "아침", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 종류는 필수입니다.")
    String dietType,

    @Schema(description = "외식/술자리 종류 (외식, 술자리)", example = "외식", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "외식/술자리 종류는 필수입니다.")
    String dietEntryType
) {} 