package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;

@Schema(description = "계획 식단 수정 요청 DTO")
public record DietPlanUpdateRequest(
    @Schema(description = "식단 계획 날짜", example = "2025-07-22")
    @NotNull(message = "식단 계획 날짜는 필수입니다.")
    LocalDate dietTime,

    @Schema(description = "음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 목록은 필수입니다.")
    @Size(min = 1, message = "음식은 한 개 이상 등록해야 합니다.")
    List<@Valid DietFoodCreateRequest> foods
) {} 