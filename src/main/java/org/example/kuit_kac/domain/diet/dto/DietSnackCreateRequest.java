package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietFoodSnackCreateRequest;

@Schema(description = "간식 식단 생성 요청 DTO")
public record DietSnackCreateRequest(
    @Schema(description = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유저 ID는 필수입니다.")
    Long userId,

    @Schema(description = "식단 이름", example = "간식1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 이름은 필수입니다.")
    String name,

    @Schema(description = "음식 목록 (중복 불가, 1개 이상)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 목록은 필수입니다.")
    @Size(min = 1, message = "음식은 한 개 이상 등록해야 합니다.")
    List<@Valid DietFoodSnackCreateRequest> foods
) {} 