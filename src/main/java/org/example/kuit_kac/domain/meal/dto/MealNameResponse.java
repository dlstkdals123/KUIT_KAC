package org.example.kuit_kac.domain.meal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.meal.model.Meal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "식단 이름 정보를 담는 응답 DTO입니다.")
public class MealNameResponse {
    @Schema(description = "식단의 고유 식별자 (ID)", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "식단 이름", example = "점심 식단", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    public static MealNameResponse from(Meal meal) {
        return new MealNameResponse(
                meal.getId(),
                meal.getName()
        );
    }
}