package org.example.kuit_kac.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

@Schema(description = "AI 식단 생성 응답")
public record AiGenerateResponse(
    @Schema(description = "생성된 식단 데이터", example = "{\"2025-08-11\": {\"아침\": [{\"name\": \"현미죽\", \"food_type\": \"NORMAL_RICE\", \"is_processed_food\": false, \"kcal\": 200, \"protein\": 6.0, \"fat\": 1.0, \"carb\": 40.0, \"sugar\": 5.0, \"unit_type\": \"그릇\", \"unit_num\": 400, \"score\": 1}]}}")
    @JsonProperty("response")
    Map<String, Map<String, List<FoodItem>>> response
) {
    @Schema(description = "음식 항목 정보")
    public record FoodItem(
        @Schema(description = "음식 이름", example = "현미죽")
        String name,
        
        @Schema(description = "음식 타입", example = "NORMAL_RICE")
        String food_type,
        
        @Schema(description = "가공식품 여부", example = "false")
        boolean is_processed_food,
        
        @Schema(description = "칼로리", example = "200")
        double kcal,
        
        @Schema(description = "단백질(g)", example = "6.0")
        double protein,
        
        @Schema(description = "지방(g)", example = "1.0")
        double fat,
        
        @Schema(description = "탄수화물(g)", example = "40.0")
        double carb,
        
        @Schema(description = "당류(g)", example = "5.0")
        double sugar,
        
        @Schema(description = "단위 타입", example = "그릇")
        String unit_type,
        
        @Schema(description = "단위 수량", example = "400")
        Long unit_num,
        
        @Schema(description = "점수", example = "1")
        Integer score
    ) {}
}