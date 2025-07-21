package org.example.kuit_kac.domain.home.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodList {
    private long foodId;
    private String name;
    private String unitType;
    private long unitNum;
    private double calorie;
}
