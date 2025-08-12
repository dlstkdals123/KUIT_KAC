package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;

@Schema(description = "계획 식단 생성 요청 DTO")
public record DietPlanCreateRequest(
    @Schema(description = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유저 ID는 필수입니다.")
    Long userId,

    @Schema(description = "식단 종류", example = "아침", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 종류는 필수입니다.")
    String dietType,

    @Schema(description = "식단 날짜", example = "2025-08-09", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "식단 날짜는 필수입니다.")
    LocalDate date,

    @Schema(description = "음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 목록은 필수입니다.")
    @Size(min = 1, message = "음식은 한 개 이상 등록해야 합니다.")
    List<@Valid DietFoodCreateRequest> foods
) {} 