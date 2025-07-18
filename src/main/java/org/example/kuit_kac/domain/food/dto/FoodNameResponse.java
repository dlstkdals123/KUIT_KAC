package org.example.kuit_kac.domain.food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.food.model.Food;

@Getter
@AllArgsConstructor
@Schema(description = "음식의 이름을 담는 응답 DTO입니다. 음식 검색에 사용됩니다.")
public class FoodNameResponse {
    @Schema(description = "음식의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "음식 이름", example = "샌드위치_닭가슴살")
    private String name;

    public static FoodNameResponse from(Food food) {
        return new FoodNameResponse(food.getId(), food.getName());
    }
}
