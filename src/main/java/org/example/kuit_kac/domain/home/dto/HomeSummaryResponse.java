package org.example.kuit_kac.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.food.model.Food;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeSummaryResponse {
    // 오늘 남은 칼로리, 오늘 섭취한 칼로리, 목표일일칼로리, 현재 체중

    private double remainingKCalorie;
    private double dailyKCalorieGoal;
    private double totalIntakeKCalorie;
    private double weight;
}
