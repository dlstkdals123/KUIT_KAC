package org.example.kuit_kac.domain.diet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietType {
    RECORD("record"),
    PLAN("plan"),
    AI_PLAN("ai_plan");

    private final String value;
}