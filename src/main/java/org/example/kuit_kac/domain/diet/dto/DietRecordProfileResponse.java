package org.example.kuit_kac.domain.diet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class DietRecordProfileResponse {
    @Schema(description = "식단의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "식단의 이름", example = "점심1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "식단의 유형", example = "점심", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dietType;

    @Schema(description = "식단의 항목 종류", example = "기록", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dietEntryType;

    @Schema(description = "식단의 시간", example = "2025-07-10T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dietTime;

    @Schema(description = "식단 정보 생성일시", example = "2025-07-10T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "식단 정보 최종 수정일시", example = "2025-07-10T12:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "식단의 총 칼로리", example = "368", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double totalKcal;

    @Schema(description = "식단에 포함된 음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FoodProfileResponse> dietFoods;

    public static DietRecordProfileResponse from(Diet diet) {
        List<FoodProfileResponse> dietFoods = diet.getDietFoods().stream()
            .map(dietFood -> FoodProfileResponse.from(dietFood, dietFood.getFood()))
            .collect(Collectors.toList());

        Double totalKcal = dietFoods.stream()
            .mapToDouble(dietFood -> {
                double quantity = dietFood.getQuantity();
                double calorie = dietFood.getCalorie();
                return quantity * calorie;
            })
            .sum();

        return new DietRecordProfileResponse(
            diet.getId(),
            diet.getName(),
            diet.getDietType().getKoreanName(),
            diet.getDietEntryType().getKoreanName(),
            diet.getDietTime(),
            diet.getCreatedAt(),
            diet.getUpdatedAt(),
            totalKcal,
            dietFoods
        );
    }
}
