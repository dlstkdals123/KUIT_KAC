package org.example.kuit_kac.domain.meal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.meal.model.MealType;
import org.example.kuit_kac.domain.meal_food.dto.MealFoodRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "새로운 끼니 생성 요청 DTO입니다. 해당 끼니에 포함될 음식 정보를 포함합니다.")
public class MealCreateRequest {

    @NotNull(message = "끼니 유형은 필수입니다.")
    @Schema(description = "끼니 유형", example = "LUNCH", allowableValues = {"BREAKFAST", "LUNCH", "DINNER", "SNACK"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private MealType mealType;

    @NotNull(message = "끼니 시간은 필수입니다.")
    @Schema(description = "끼니 시간 (YYYY-MM-DDTHH:MM:SS 형식)", example = "2025-07-12T19:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime mealTime;

    @NotEmpty(message = "끼니에는 최소 하나 이상의 음식이 포함되어야 합니다.")
    @Schema(description = "해당 끼니에 포함될 음식 목록")
    private List<MealFoodRequest> mealFoods;
}