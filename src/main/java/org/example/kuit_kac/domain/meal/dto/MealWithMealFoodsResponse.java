package org.example.kuit_kac.domain.meal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal.model.MealType;
import org.example.kuit_kac.domain.meal_food.dto.MealFoodResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Schema(description = "끼니와 해당 끼니에 포함된 음식 목록 정보를 담는 응답 DTO입니다.")
public class MealWithMealFoodsResponse {
    @Schema(description = "끼니의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "연결된 식단의 고유 식별자 (Diet ID)", example = "101")
    private Long dietId;

    @Schema(description = "끼니 유형", example = "LUNCH", allowableValues = {"BREAKFAST", "LUNCH", "DINNER", "SNACK"})
    private MealType mealType;

    @Schema(description = "끼니를 섭취한 시간", example = "2025-07-10T12:30:00")
    private LocalDateTime mealTime;

    @Schema(description = "끼니 정보 생성일시", example = "2025-07-10T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "끼니 정보 최종 수정일시", example = "2025-07-10T12:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "해당 끼니에 포함되는 음식 목록 (MealFood 정보 포함)")
    private List<MealFoodResponse> foodItems;

    public static MealWithMealFoodsResponse from(Meal meal) {
        List<MealFoodResponse> foodItems = meal.getMealFoods().stream()
                .map(MealFoodResponse::from)
                .collect(Collectors.toList());

        return new MealWithMealFoodsResponse(
                meal.getId(),
                meal.getDiet().getId(),
                meal.getMealType(),
                meal.getMealTime(),
                meal.getCreatedAt(),
                meal.getUpdatedAt(),
                foodItems
        );
    }
}