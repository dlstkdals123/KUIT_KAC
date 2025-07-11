package org.example.kuit_kac.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다."),

    DIET_TYPE_INVALID("DIET_001", "유효하지 않은 식단 유형입니다. 유효한 값: RECORD, PLAN, AI_PLAN"),
    DIET_USER_ID_NOT_FOUND("DIET_002", "해당 사용자의 식단을 찾을 수 없습니다."),
    DIET_USER_ID_AND_DIET_TYPE_NOT_FOUND("DIET_003", "사용자, 식단 유형이 모두 일치하는 식단을 찾을 수 없습니다."),
    DIET_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND("DIET_004", "사용자, 식단 유형, 날짜가 모두 일치하는 식단을 찾을 수 없습니다."),

    MEAL_DIET_NOT_FOUND("MEAL_001", "해당 식단에 해당하는 끼니를 찾을 수 없습니다."),

    FOOD_MEAL_ID_NOT_FOUND("FOOD_001", "해당 끼니에 해당하는 음식을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
