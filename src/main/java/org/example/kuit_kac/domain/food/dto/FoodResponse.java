package org.example.kuit_kac.domain.food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import org.example.kuit_kac.domain.food.model.Food;

@Getter
@AllArgsConstructor
@Schema(description = "음식의 모든 정보를 담는 응답 DTO입니다. 음식 검색에 사용됩니다.")
public class FoodResponse {
    @Schema(description = "음식의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "음식 이름", example = "국밥_돼지머리")
    private String name;

    @Schema(description = "음식의 단위 타입", example = "그릇", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unitType;

    @Schema(description = "음식의 단위 수량", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long unitNum;

    @Schema(description = "음식의 타입", example = "NORMAL_GRAIN_AND_TUBER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String foodType;

    @Schema(description = "음식의 가공식품 여부 (가능한 값: true(가공식품), false(일반식품))", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isProcessedFood;

    @Schema(description = "음식의 칼로리 (단위: kcal)", example = "137.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double calorie;

    @Schema(description = "음식의 탄수화물 (단위: g)", example = "25.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double carbohydrate;

    @Schema(description = "음식의 단백질 (단위: g)", example = "8.2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double protein;

    @Schema(description = "음식의 지방 (단위: g)", example = "3.1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double fat;

    @Schema(description = "음식의 설탕 (단위: g)", example = "2.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double sugar;

    @Schema(description = "음식의 점수 (가능한 값: 0(위험), 1(적당), 2(양호))", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int score;

    @Schema(description = "음식 정보 생성일시 (형식: YYYY-MM-DDTHH:MM:SS)", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "음식 정보 최종 수정일시 (형식: YYYY-MM-DDTHH:MM:SS)", example = "2023-01-01T00:00:00")
    private LocalDateTime updatedAt;

    public static FoodResponse from(Food food) {
        return new FoodResponse(
            food.getId(), 
            food.getName(), 
            food.getUnitType(),
            food.getUnitNum(),
            food.getFoodType().getValue(), 
            food.getIsProcessedFood(), 
            food.getCalorie(),
            food.getCarbohydrate(),
            food.getProtein(),
            food.getFat(),
            food.getSugar(),
            food.getScore(),
            food.getCreatedAt(), 
            food.getUpdatedAt());
    }
}