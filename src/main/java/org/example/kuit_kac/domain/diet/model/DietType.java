package org.example.kuit_kac.domain.diet.model;

import org.example.kuit_kac.global.util.EnumConverter;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

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

    public static DietType getDietType(String dietType) {
        DietType fromKorean = EnumConverter.fromKoreanDietType(dietType);
        if (fromKorean != null) return fromKorean;
        try {
            return DietType.valueOf(dietType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }
    }
}
