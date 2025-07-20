package org.example.kuit_kac.domain.diet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietEntryType {
    RECORD("RECORD", "기록"),
    PLAN("PLAN", "계획"),
    AI_PLAN("AI_PLAN", "AI 계획"),
    DINING_OUT("DINING_OUT", "외식"),
    DRINKING("DRINKING", "술자리");

    private final String value;
    private final String koreanName;
}