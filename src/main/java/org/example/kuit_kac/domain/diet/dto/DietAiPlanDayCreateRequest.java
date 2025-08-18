package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "계획 식단 생성 요청 DTO")
public record DietAiPlanDayCreateRequest(
    @Schema(description = "계획 식단 날짜 (형식: YYYY-MM-DD)", example = "2025-08-09", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "계획 식단 날짜는 필수입니다.")
    LocalDate dietDate,

    @Schema(description = "계획 식단 목록 (1개 이상)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "계획 식단 목록은 필수입니다.")
    @Size(min = 1, message = "계획 식단은 한 개 이상 등록해야 합니다.")
    List<@Valid DietAiPlanCreateRequest> diets
) {} 