package org.example.kuit_kac.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "AI 식단 생성 요청 DTO")
public record DietActivityCreateRequest(
    @Schema(description = "외식/술자리 날짜", example = "2025-08-09", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "외식/술자리 날짜는 필수입니다.")
    LocalDate dietDate,

    @Schema(description = "외식/술자리 종류", example = "외식", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "외식/술자리 종류는 필수입니다.")
    String dietEntryType
) {} 