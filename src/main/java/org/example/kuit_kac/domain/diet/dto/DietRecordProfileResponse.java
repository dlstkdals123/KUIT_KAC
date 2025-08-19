package org.example.kuit_kac.domain.diet.dto;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.global.util.TimeGenerator;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodProfileResponse;
import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;
import org.example.kuit_kac.domain.food.model.FoodStatusType;
import java.util.stream.Collectors;

@Schema(description = "식단 기록 응답 DTO")
public record DietRecordProfileResponse(
    @Schema(description = "식단의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "식단의 이름", example = "점심1", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "식단의 날짜 (형식: YYYY-MM-DD)", example = "2025-08-09", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dietDate,

    @Schema(description = "식단의 유형 (아침, 점심, 저녁, 간식, 나만의 식단)", example = "점심", requiredMode = Schema.RequiredMode.REQUIRED)
    String dietType,

    @Schema(description = "식단의 항목 종류 (기록, 단식, 계획, AI 계획, 외식, 술자리)", example = "기록", requiredMode = Schema.RequiredMode.REQUIRED)
    String dietEntryType,

    @Schema(description = "식단의 상태 (양호, 적당, 위험)", example = "양호", requiredMode = Schema.RequiredMode.REQUIRED)
    String foodStatusType,

    @Schema(description = "식단 정보 생성일시 (형식: YYYY-MM-DDTHH:MM:SS)")
    LocalDateTime createdAt,

    @Schema(description = "식단 정보 최종 수정일시 (형식: YYYY-MM-DDTHH:MM:SS)")
    LocalDateTime updatedAt,

    @Schema(description = "식단의 총 칼로리 (단위: kcal)", example = "368", requiredMode = Schema.RequiredMode.REQUIRED)
    Double totalKcal,

    @Schema(description = "식단에 포함된 음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<DietFoodProfileResponse> dietFoods
) {
    public static DietRecordProfileResponse todayFrom(Diet diet) {
        List<DietFoodProfileResponse> dietFoodProfiles = diet.getDietFoods().stream()
                .filter(dietFood -> TimeGenerator.isToday(dietFood.getDietTime()))
                .map(DietFoodProfileResponse::from)
                .toList();

        if (dietFoodProfiles.isEmpty()) {
            return null;
        }

        return from(dietFoodProfiles, diet);
    }

    public static DietRecordProfileResponse from(Diet diet) {
        List<DietFoodProfileResponse> dietFoodProfiles = diet.getDietFoods().stream()
                .map(DietFoodProfileResponse::from)
                .toList();

        
        return from(dietFoodProfiles, diet);
    }

    private static DietRecordProfileResponse from(List<DietFoodProfileResponse> dietFoodProfiles, Diet diet) {
        double totalKcal = dietFoodProfiles.stream()
                .mapToDouble(dietFood -> dietFood.quantity() * dietFood.food().getCalorie())
                .sum();

        List<FoodProfileResponse> dietFoodProfileResponses = dietFoodProfiles.stream()
                .map(DietFoodProfileResponse::food)
                .collect(Collectors.toList());

        FoodStatusType foodStatusType = FoodStatusType.getFoodStatusType(dietFoodProfileResponses);

        return new DietRecordProfileResponse(
                diet.getId(),
                diet.getName(),
                diet.getDietDate(),
                diet.getDietType() != null ? diet.getDietType().getKoreanName() : null,
                diet.getDietEntryType() != null ? diet.getDietEntryType().getKoreanName() : null,
                foodStatusType.getKoreanName(),
                diet.getCreatedAt(),
                diet.getUpdatedAt(),
                totalKcal,
                dietFoodProfiles
        );
    }
}
