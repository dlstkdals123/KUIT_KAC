package org.example.kuit_kac.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "그날의 섭취 영양 정보 DTO")
public class HomeNutritionResponse {
    // 탄수화물, 단백질, 당류, 지방 각각에서 섭취목표량(g), 섭취량(g), 목표량 대비 섭취량 비율 반환
    @Schema(description = "탄수화물 섭취목표량(g)")
    private int carbGoal;

    @Schema(description = "단백질 섭취목표량(g)")
    private int proteinGoal;

    @Schema(description = "당류 섭취목표량(g)")
    private int sugarGoal;

    @Schema(description = "지방 섭취목표량(g)")
    private int fatGoal;

    @Schema(description = "탄수화물 섭취량(g)")
    private int carbTaken;

    @Schema(description = "단백질 섭취량(g)")
    private int proteinTaken;

    @Schema(description = "당류 섭취량(g)")
    private int sugarTaken;

    @Schema(description = "지방 섭취량(g)")
    private int fatTaken;

    @Schema(description = "탄수화물 목표량 대비 섭취량 비율(%)")
    private int carbRatio;

    @Schema(description = "단백질 목표량 대비 섭취량 비율(%)")
    private int proteinRatio;

    @Schema(description = "당류 목표량 대비 섭취량 비율(%)")
    private int sugarRatio;

    @Schema(description = "지방 목표량 대비 섭취량 비율(%)")
    private int fatRatio;
}
