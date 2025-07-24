package org.example.kuit_kac.domain.diet.model;

import org.example.kuit_kac.global.util.EnumConverter;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietEntryType {
    RECORD("RECORD", "기록"),
    FASTING("FASTING", "단식"),
    PLAN("PLAN", "계획"),
    AI_PLAN("AI_PLAN", "AI 계획"),
    DINING_OUT("DINING_OUT", "외식"),
    DRINKING("DRINKING", "술자리");

    private final String value;
    private final String koreanName;

    public static DietEntryType getDietEntryType(String dietEntryType) {
        DietEntryType fromKorean = EnumConverter.fromKoreanDietEntryType(dietEntryType);
        if (fromKorean != null) return fromKorean;
        try {
            return DietEntryType.valueOf(dietEntryType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_INVALID);
        }
    }
}