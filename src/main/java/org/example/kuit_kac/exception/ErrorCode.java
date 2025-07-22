package org.example.kuit_kac.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 유저(User) 관련 에러 코드
    USER_NOT_FOUND("USER_001", "존재하지 않는 유저입니다."),

    // 식단(Diet) 관련 에러 코드
    DIET_NOT_FOUND("DIET_001", "존재하지 않는 식단입니다."),
    DIET_TYPE_INVALID("DIET_002", "유효하지 않은 식단 유형입니다."),

    // 음식(Food) 관련 에러 코드
    FOOD_NOT_FOUND("FOOD_001", "존재하지 않는 음식입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
