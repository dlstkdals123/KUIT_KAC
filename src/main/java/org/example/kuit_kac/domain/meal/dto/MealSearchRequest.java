package org.example.kuit_kac.domain.meal.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.meal.model.MealType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "끼니 기록 조회를 위한 검색 조건 DTO")
public class MealSearchRequest {

    @Parameter(description = "끼니 기록을 조회할 사용자의 고유 ID. 선택 사항입니다.", example = "101")
    private Long userId;

    @Parameter(description = "조회할 식단 유형 (예: BREAKFAST, LUNCH, DINNER, SNACK, TEMPLATE). 선택 사항입니다.", example = "RECORD")
    private MealType mealType;
}
