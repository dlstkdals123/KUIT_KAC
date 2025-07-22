package org.example.kuit_kac.domain.diet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietType {
    BREAKFAST("BREAKFAST", "아침"),
    LUNCH("LUNCH", "점심"),
    DINNER("DINNER", "저녁"),
    SNACK("SNACK", "간식"),
    TEMPLATE("TEMPLATE", "나만의 식단");

    private final String value;
    private final String koreanName;
}
