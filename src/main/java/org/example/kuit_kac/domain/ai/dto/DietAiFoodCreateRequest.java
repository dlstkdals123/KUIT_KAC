package org.example.kuit_kac.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;

@Schema(description = "식단에 포함될 음식 생성 요청 DTO")
public record DietAiFoodCreateRequest(
    @Schema(description = "음식 섭취량", example = "1.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "음식 섭취량은 0보다 커야 합니다.")
    @DecimalMin(value = "0.1", message = "음식 섭취량은 0.1 이상이어야 합니다.")
    @DecimalMax(value = "100", message = "음식 섭취량은 100 이하이어야 합니다.")
    double quantity,

    FoodProfileResponse food
) {
    public static DietAiFoodCreateRequest from(double quantity, FoodProfileResponse food) {
        return new DietAiFoodCreateRequest(
            quantity,
            food
        );
    }
} 