package org.example.kuit_kac.domain.diet.dto;

import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "템플릿 식단 수정 요청 DTO")
public record DietTemplateUpdateRequest(
    @Schema(description = "식단 이름", example = "나만의 다이어트 식단", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 이름은 필수입니다.")
    String name,

    @Schema(description = "음식 목록 (중복 불가, 1개 이상)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 목록은 필수입니다.")
    @Size(min = 1, message = "음식은 한 개 이상 등록해야 합니다.")
    List<@Valid DietFoodCreateRequest> foods
) {} 