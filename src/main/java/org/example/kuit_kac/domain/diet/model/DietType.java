package org.example.kuit_kac.domain.diet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietType {
    RECORD("RECORD"),
    PLAN("PLAN"),
    AI_PLAN("AI_PLAN"),
    FASTING("FASTING"),
    DINING_OUT("DINING_OUT"),
    DRINKING("DRINKING");

    private final String value;
}