package org.example.kuit_kac.domain.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodSummary {
    private long foodId;
    private double quantity;
    private double carbohydrateRatio;
    private double proteinRatio;
    private double fatRatio;
    private double totalCalorie;
}
