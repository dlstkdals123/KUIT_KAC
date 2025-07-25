package org.example.kuit_kac.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.food.model.Food;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeSummaryResponse {
    // 일일섭취목표, 오늘 먹은 음식들의 칼로리, 현재 체중, 남은 칼로리

    private double dailyKCalorieGoal;
    private double totalIntakeKCalorie;
    private double weight;
    private double remainingKCalorie;
}
