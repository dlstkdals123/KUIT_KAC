package org.example.kuit_kac.global.util;

import java.util.List;

import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;

public class FoodScoreStatus {
    public static double calculateAverageScore(List<FoodProfileResponse> foods) {
        if (foods == null || foods.isEmpty()) {
            return 0;
        }
        
        double totalScore = foods.stream()
                .mapToInt(food -> food.getScore())
                .sum();
                
        return totalScore / foods.size();
    }
}
