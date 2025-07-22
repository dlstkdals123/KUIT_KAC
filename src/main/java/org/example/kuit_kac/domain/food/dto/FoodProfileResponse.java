package org.example.kuit_kac.domain.food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.food.model.Food;

@Getter
@AllArgsConstructor
@Schema(description = "음식의 이름을 담는 응답 DTO입니다. 음식 검색에 사용됩니다.")
public class FoodProfileResponse {
    @Schema(description = "음식의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "음식 이름", example = "국밥_돼지머리")
    private String name;

    @Schema(description = "음식의 양", example = "0.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double quantity;

    @Schema(description = "음식의 단위 타입", example = "그릇", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unitType;

    @Schema(description = "음식의 타입", example = "밥류", requiredMode = Schema.RequiredMode.REQUIRED)
    private String foodType;

    @Schema(description = "음식의 가공식품 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isProcessedFood;

    @Schema(description = "음식의 칼로리", example = "137.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double calorie;

    @Schema(description = "음식 정보 생성일시", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "음식 정보 최종 수정일시", example = "2023-01-01T00:00:00")
    private LocalDateTime updatedAt;

    public static FoodProfileResponse from(DietFood dietFood) {
        Food food = dietFood.getFood(); 
        return new FoodProfileResponse(
            food.getId(), 
            food.getName(), 
            dietFood.getQuantity(),
            food.getUnitType(), 
            food.getFoodType().getKoreanName(), 
            food.getIsProcessedFood(), 
            food.getCalorie(), 
            food.getCreatedAt(), 
            food.getUpdatedAt());
    }
}
