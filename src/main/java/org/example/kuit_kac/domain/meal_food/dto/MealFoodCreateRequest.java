package org.example.kuit_kac.domain.meal_food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "끼니에 포함될 단일 음식 및 섭취량 정보 요청 DTO입니다.")
public class MealFoodCreateRequest {

    @NotNull(message = "음식 ID는 필수입니다.")
    @Schema(description = "음식의 고유 식별자 (Food ID)", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long foodId;

    @NotNull(message = "섭취량은 필수입니다.")
    @DecimalMin(value = "0.1", message = "섭취량은 0.1 이상이어야합니다.")
    @Schema(description = "음식의 섭취량", example = "1.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private double quantity;
}