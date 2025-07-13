package org.example.kuit_kac.domain.meal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.meal.model.MealType;
import org.example.kuit_kac.domain.meal_food.dto.MealFoodUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "끼니 정보 전체 업데이트를 위한 요청 DTO입니다. 끼니의 모든 필드와 연결된 음식 목록을 포함합니다.")
public class MealUpdateRequest {

    @Schema(description = "끼니 유형", example = "LUNCH", allowableValues = {"BREAKFAST", "LUNCH", "DINNER", "SNACK"})
    private MealType mealType;

    @Schema(description = "끼니를 섭취한 시간", example = "2025-07-10T12:30:00")
    private LocalDateTime mealTime;

    @Schema(description = "끼니에 포함될 음식 목록입니다. 기존 음식 정보는 모두 이 목록으로 대체됩니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MealFoodUpdateRequest> foods;
}