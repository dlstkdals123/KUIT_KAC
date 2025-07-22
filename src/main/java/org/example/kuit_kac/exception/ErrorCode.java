package org.example.kuit_kac.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 식단(Diet) 관련 에러 코드
    DIET_TYPE_INVALID("DIET_001", "유효하지 않은 식단 유형입니다. 유효한 값: RECORD, PLAN, AI_PLAN, FASTING, DINING_OUT, DRINKING");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
