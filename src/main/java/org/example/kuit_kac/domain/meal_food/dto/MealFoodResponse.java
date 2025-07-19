package org.example.kuit_kac.domain.meal_food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.food.dto.FoodResponse;
import org.example.kuit_kac.domain.meal_food.model.MealAifood;
import org.example.kuit_kac.domain.meal_food.model.MealFood;

@Getter
@AllArgsConstructor
@Schema(description = "끼니에 포함된 개별 음식의 상세 정보 및 섭취량을 담는 응답 DTO입니다.")
public class MealFoodResponse {
    @Schema(description = "끼니_음식의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long mealFoodId;

    @Schema(description = "해당 음식의 섭취량", example = "200.0")
    private double quantity;

    @Schema(description = "음식 상세 정보")
    private FoodResponse food;

    public static MealFoodResponse from(MealFood mealFood) {
        FoodResponse foodResponse = FoodResponse.from(mealFood.getFood());
        return new MealFoodResponse(
                mealFood.getId(),
                mealFood.getQuantity(),
                foodResponse
        );
    }

    public static MealFoodResponse from(MealAifood mealAifood) {
        FoodResponse aifoodResponse = FoodResponse.from(mealAifood.getAifood());
        return new MealFoodResponse(
                mealAifood.getId(),
                mealAifood.getQuantity(),
                aifoodResponse
        );
    }
}