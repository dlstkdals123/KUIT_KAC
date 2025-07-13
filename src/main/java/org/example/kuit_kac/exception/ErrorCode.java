package org.example.kuit_kac.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 사용자 관련 에러 코드
    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다."),

    // 식단(Diet) 관련 에러 코드
    DIET_TYPE_INVALID("DIET_001", "유효하지 않은 식단 유형입니다. 유효한 값: RECORD, PLAN, AI_PLAN"),
    DIET_USER_ID_NOT_FOUND("DIET_002", "해당 사용자의 식단을 찾을 수 없습니다."),
    DIET_USER_ID_AND_DIET_TYPE_NOT_FOUND("DIET_003", "사용자, 식단 유형이 모두 일치하는 식단을 찾을 수 없습니다."),
    DIET_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND("DIET_004", "사용자, 식단 유형, 날짜가 모두 일치하는 식단을 찾을 수 없습니다."),
    FASTING_DIET_CANNOT_CONTAIN_MEALS("DIET_005", "단식 식단에는 끼니 정보가 포함될 수 없습니다."),
    MEAL_EMPTY("DIET_006", "일반 식단에는 최소 하나 이상의 끼니가 포함되어야 합니다."),
    DIET_NOT_FOUND("DIET_007", "해당 식단을 찾을 수 없습니다."),

    // 식단 템플릿(DietTemplate) 관련 에러 코드
    DIET_TEMPLATE_NOT_FOUND("DIET_TEMPLATE_001", "나만의 식단 템플릿을 찾을 수 없습니다."),

    // 끼니(Meal) 관련 에러 코드
    MEAL_DIET_NOT_FOUND("MEAL_001", "해당 식단에 해당하는 끼니를 찾을 수 없습니다."),
    MEAL_NOT_FOUND("MEAL_002", "끼니를 찾을 수 없습니다."),

    // 끼니 음식(MealFood) 관련 에러 코드
    MEAL_FOOD_EMPTY("MEAL_FOOD_001", "끼니에는 최소 하나 이상의 음식이 포함되어야 합니다."),

    // 음식(Food) 관련 에러 코드
    FOOD_NOT_FOUND("FOOD_001", "해당 음식을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
