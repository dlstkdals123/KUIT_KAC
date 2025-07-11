package org.example.kuit_kac.domain.meal.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MealType {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("lunch"),
    SNACK("snack");

    private final String value;
}
