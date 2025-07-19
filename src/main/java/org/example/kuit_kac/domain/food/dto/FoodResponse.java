package org.example.kuit_kac.domain.food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.food.model.Food;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "음식 정보를 담는 응답 DTO입니다. 음식 조회, 등록 등의 API 응답에 사용됩니다.")
public class FoodResponse {
    @Schema(description = "음식의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "음식 이름", example = "닭가슴살")
    private String name;

    @Schema(description = "단위 유형 (예: 개, 공기, 캔)", example = "캔")
    private String unitType;

    @Schema(description = "단위당 개수 또는 중량 (예: 1공기 = 250g, 1캔 = 330ml, 1개 = 100g)", example = "250")
    private Long unitNum;

    @Schema(description = "음식대분류명 (예: 밥류, 과자류·빵류 또는 떡류)", example = "밥류")
    private String foodType;

    @Schema(description = "가공식품 여부", example = "true")
    private Boolean isProcessedFood;

    @Schema(description = "칼로리 (kcal)", example = "165.0")
    private Double calorie;

    @Schema(description = "탄수화물 (g)", example = "0.0")
    private Double carbohydrate;

    @Schema(description = "단백질 (g)", example = "31.0")
    private Double protein;

    @Schema(description = "지방 (g)", example = "3.6")
    private Double fat;

    @Schema(description = "당류 (g)", example = "0.0")
    private Double sugar;

    @Schema(description = "음식 정보 생성일시", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "음식 정보 최종 수정일시", example = "2023-01-01T00:00:00")
    private LocalDateTime updatedAt;

    public static FoodResponse from(Food food) {
        return new FoodResponse(
                food.getId(),
                food.getName(),
                food.getUnitType(),
                food.getUnitNum(),
                food.getFoodType(),
                food.getIsProcessedFood(),
                food.getCalorie(),
                food.getCarbohydrate(),
                food.getProtein(),
                food.getFat(),
                food.getSugar(),
                food.getCreatedAt(),
                food.getUpdatedAt()
        );
    }
}