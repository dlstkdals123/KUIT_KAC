package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.meal.dto.MealWithMealFoodsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Schema(description = "식단과 해당 식단에 포함된 끼니 및 음식 상세 정보를 담는 응답 DTO입니다.")
public class DietWithMealsAndFoodsResponse {
    @Schema(description = "식단의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "연결된 사용자의 고유 식별자 (User ID)", example = "101")
    private Long userId;

    @Schema(description = "식단 유형", example = "RECORD", allowableValues = {"RECORD", "PLAN", "AI_PLAN", "FASTING", "DINING_OUT", "DRINKING"})
    private DietType dietType;

    @Schema(description = "식단 날짜", example = "2025-07-12")
    private LocalDate dietDate;

    @Schema(description = "식단 정보 생성일시", example = "2025-07-12T09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "식단 정보 최종 수정일시", example = "2025-07-12T09:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "해당 식단에 포함된 끼니 목록 (각 끼니에 음식 상세 포함)")
    private List<MealWithMealFoodsResponse> meals;

    public static DietWithMealsAndFoodsResponse from(Diet diet) {
        List<MealWithMealFoodsResponse> mealResponses = diet.getMeals().stream()
                .map(MealWithMealFoodsResponse::from)
                .collect(Collectors.toList());

        return new DietWithMealsAndFoodsResponse(
                diet.getId(),
                diet.getUser().getId(),
                diet.getDietType(),
                diet.getDietDate(),
                diet.getCreatedAt(),
                diet.getUpdatedAt(),
                mealResponses
        );
    }
}