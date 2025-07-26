package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;

@Schema(description = "식단 기록 수정 요청 DTO")
public record DietRecordUpdateRequest(
    @Schema(description = "식단 이름", example = "아침식단", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 이름은 필수입니다.")
    String name,

    @Schema(description = "식단 음식 섭취 시간", example = "2025-07-22T12:00:00")
    @NotNull(message = "식단 음식 섭취 시간은 필수입니다.")
    LocalDateTime dietTime,

    @Schema(description = "음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 목록은 필수입니다.")
    @Size(min = 1, message = "음식은 한 개 이상 등록해야 합니다.")
    List<@Valid DietFoodCreateRequest> foods
) {} 