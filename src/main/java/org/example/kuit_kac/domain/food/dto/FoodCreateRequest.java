package org.example.kuit_kac.domain.food.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;

public record FoodCreateRequest(
    @Getter
    @Schema(description = "음식 이름", example = "현미죽")
    @NotNull(message = "음식 이름은 필수입니다")
    String name,
    
    @Schema(description = "단위 타입", example = "그릇")
    @NotNull(message = "단위 타입은 필수입니다")
    @JsonProperty("unit_type")
    String unitType,
    
    @Schema(description = "단위 수량", example = "400")
    @NotNull(message = "단위 수량은 필수입니다")
    @JsonProperty("unit_num")
    Long unitNum,
    
    @Schema(description = "음식 타입", example = "NORMAL_RICE")
    @NotNull(message = "음식 타입은 필수입니다")
    @JsonProperty("food_type")
    String foodType,
    
    @Schema(description = "가공식품 여부 (가능한 값: true(가공식품), false(일반식품))", example = "false")
    @NotNull(message = "가공식품 여부는 필수입니다")
    @JsonProperty("is_processed_food")
    Boolean isProcessedFood,
    
    @Schema(description = "칼로리 (단위: kcal)", example = "200.0")
    @NotNull(message = "칼로리는 필수입니다")
    @JsonProperty("kcal")
    Double kcal,
    
    @Schema(description = "탄수화물 (단위: g)", example = "40.0")
    @NotNull(message = "탄수화물은 필수입니다")
    @JsonProperty("carb")
    Double carb,
    
    @Schema(description = "단백질 (단위: g)", example = "6.0")
    @NotNull(message = "단백질은 필수입니다")
    @JsonProperty("protein")
    Double protein,
    
    @Schema(description = "지방 (단위: g)", example = "1.0")
    @NotNull(message = "지방은 필수입니다")
    @JsonProperty("fat")
    Double fat,
    
    @Schema(description = "당류 (단위: g)", example = "5.0")
    @NotNull(message = "당류는 필수입니다")
    @JsonProperty("sugar")
    Double sugar,
    
    @Schema(description = "점수 (가능한 값: 0(위험), 1(적당), 2(양호))", example = "1")
    @NotNull(message = "점수는 필수입니다")
    @JsonProperty("score")
    @Min(value = 0, message = "점수는 0 이상이어야 합니다")
    @Max(value = 2, message = "점수는 2 이하이어야 합니다")
    Integer score
) {}
