package org.example.kuit_kac.domain.food.model;

import java.util.List;

import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;
import org.example.kuit_kac.global.util.FoodScoreStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodStatusType {
    HEALTHY("HEALTHY", "양호"),
    NORMAL("NORMAL", "적당"),
    DANGEROUS("DANGEROUS", "위험");

    private final String value;
    private final String koreanName;

    public static FoodStatusType getFoodStatusType(List<FoodProfileResponse> foodProfiles) {
        double score = FoodScoreStatus.calculateAverageScore(foodProfiles);

        if (score < 0.8) {
            return DANGEROUS;
        } else if (score >= 0.8 && score < 1.5) {
            return NORMAL;
        } else {
            return HEALTHY;
        }
    }
}
