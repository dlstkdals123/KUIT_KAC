package org.example.kuit_kac.domain.diet_food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

@Schema(description = "식단에 포함될 음식 생성 요청 DTO")
public record DietFoodCreateRequest(
    //TODO: 하나의 요청에 음식 ID가 중복되어서는 안됩니다.
    @Schema(description = "음식 ID (중복 불가)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "음식 ID는 필수입니다.")
    Long foodId,

    @Schema(description = "음식 섭취량", example = "1.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "음식 섭취량은 0보다 커야 합니다.")
    @DecimalMin(value = "0.1", message = "음식 섭취량은 0.1 이상이어야 합니다.")
    @DecimalMax(value = "100", message = "음식 섭취량은 100 이하이어야 합니다.")
    double quantity
) {} 