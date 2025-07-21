package org.example.kuit_kac.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.home.model.FoodSummary;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeSummaryResponse {
    private List<FoodSummary> foods;

    private double totalIntakeCalorie;
    private double remainingCalorie;

    private double carbohydrateRatio;
    private double proteinRatio;
    private double fatRatio;
}
