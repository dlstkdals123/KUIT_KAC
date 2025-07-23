package org.example.kuit_kac.domain.diet.dto;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.global.util.TimeRange;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodProfileResponse;

@Schema(description = "식단 기록 응답 DTO")
public record DietRecordProfileResponse(
    @Schema(description = "식단의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "식단의 이름", example = "점심1", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "식단의 유형", example = "점심", requiredMode = Schema.RequiredMode.REQUIRED)
    String dietType,

    @Schema(description = "식단의 항목 종류", example = "기록", requiredMode = Schema.RequiredMode.REQUIRED)
    String dietEntryType,

    @Schema(description = "식단 정보 생성일시", example = "2025-07-10T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "식단 정보 최종 수정일시", example = "2025-07-10T12:00:00")
    LocalDateTime updatedAt,

    @Schema(description = "식단의 총 칼로리", example = "368", requiredMode = Schema.RequiredMode.REQUIRED)
    Double totalKcal,

    @Schema(description = "식단에 포함된 음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<DietFoodProfileResponse> dietFoods
) {
    public static DietRecordProfileResponse from(Diet diet, TimeRange timeRange) {
        List<DietFoodProfileResponse> foodProfiles = diet.getDietFoods().stream()
                .filter(dietFood -> dietFood.getDietTime().isAfter(timeRange.start()) && dietFood.getDietTime().isBefore(timeRange.end()))
                .map(DietFoodProfileResponse::from)
                .toList();

        if (foodProfiles.isEmpty()) {
            return null;
        }

        return from(foodProfiles, diet);
    }

    public static DietRecordProfileResponse from(Diet diet) {
        List<DietFoodProfileResponse> foodProfiles = diet.getDietFoods().stream()
                .map(DietFoodProfileResponse::from)
                .toList();

        return from(foodProfiles, diet);
    }

    private static DietRecordProfileResponse from(List<DietFoodProfileResponse> foodProfiles, Diet diet) {
        double totalKcal = foodProfiles.stream()
                .mapToDouble(dietFood -> dietFood.quantity() * dietFood.food().getCalorie())
                .sum();

        return new DietRecordProfileResponse(
                diet.getId(),
                diet.getName(),
                diet.getDietType() != null ? diet.getDietType().getKoreanName() : null,
                diet.getDietEntryType() != null ? diet.getDietEntryType().getKoreanName() : null,
                diet.getCreatedAt(),
                diet.getUpdatedAt(),
                totalKcal,
                foodProfiles
        );
    }
}
