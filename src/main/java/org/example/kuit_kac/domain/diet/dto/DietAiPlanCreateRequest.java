package org.example.kuit_kac.domain.diet.dto;

import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietAifoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "AI 계획 생성 요청 DTO")
public record DietAiPlanCreateRequest(
    @Schema(description = "식단 종류", example = "아침", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 종류는 필수입니다.")
    String dietType,

    @Schema(description = "음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 목록은 필수입니다.")
    List<@Valid DietFoodCreateRequest> foods,
    
    @Schema(description = "AI 음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "AI 음식 목록은 필수입니다.")
    List<@Valid DietAifoodCreateRequest> aiDietFoods
) {} 