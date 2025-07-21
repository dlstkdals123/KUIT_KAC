package org.example.kuit_kac.domain.home.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyCalorieTarget {
    private double bmr;
    private double dailyWeightLossCalorie;
    private double targetIntakeCalorie;
}
