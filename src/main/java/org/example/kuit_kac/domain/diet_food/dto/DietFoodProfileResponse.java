package org.example.kuit_kac.domain.diet_food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;

import java.time.LocalDateTime;

@Schema(description = "식단에 포함된 음식 상세 정보 DTO")
public record DietFoodProfileResponse(
    @Schema(description = "음식 섭취 시간") 
    LocalDateTime dietTime,

    @Schema(description = "음식 섭취 양") 
    Double quantity,

    @Schema(description = "음식 정보") 
    FoodProfileResponse food
) {
    public static DietFoodProfileResponse from(DietFood dietFood) {
        return new DietFoodProfileResponse(
                dietFood.getDietTime(),
                dietFood.getQuantity(),
                FoodProfileResponse.from(dietFood.getFood())
        );
    }
} 