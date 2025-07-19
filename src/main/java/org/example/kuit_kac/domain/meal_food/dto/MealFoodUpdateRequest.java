package org.example.kuit_kac.domain.meal_food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "끼니에 포함될 단일 음식 정보 요청 DTO")
public class MealFoodUpdateRequest {
    @Schema(description = "음식의 고유 식별자 (Food ID)", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long foodId;

    @Schema(description = "음식의 양 (n접시 또는 ml)", example = "1.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private double quantity;
}