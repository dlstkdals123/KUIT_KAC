package org.example.kuit_kac.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeNutritionResponse {
    // 탄수화물, 단백질, 당류, 지방 각각에서 섭취목표량(g), 섭취량(g), 목표량 대비 섭취량 비율 반환
    private int carbGoal;
    private int proteinGoal;
    private int sugarGoal;
    private int fatGoal;

    private int carbTaken;
    private int proteinTaken;
    private int sugarTaken;
    private int fatTaken;

    private int carbRatio;
    private int proteinRatio;
    private int sugarRatio;
    private int fatRatio;
}
