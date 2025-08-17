package org.example.kuit_kac.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // ENUM 에러 코드
    GENDER_TYPE_INVALID("GENDER_TYPE_001", "유효하지 않은 성별 타입입니다."),

    DIET_TYPE_INVALID("DIET_TYPE_001", "유효하지 않은 식단 타입입니다."),

    DIET_ENTRY_TYPE_INVALID("DIET_ENTRY_TYPE_001", "유효하지 않은 식단 항목 타입입니다."),
    DIET_ENTRY_TYPE_MUST_HAVE_FOOD("DIET_004", "해당 항목 종류에는 최소 하나 이상의 음식을 포함해야 합니다."),
    DIET_ENTRY_TYPE_NOT_ALLOWED("DIET_005", "해당 항목 종류는 허용되지 않습니다."),

    FOOD_TYPE_INVALID("FOOD_TYPE_001", "유효하지 않은 음식 타입입니다."),

    INTENSITY_INVALID("INTENSITY_001", "유효하지 않은 강도 타입입니다."),

    TARGET_MUSCLE_TYPE_INVALID("TARGET_MUSCLE_TYPE_001", "유효하지 않은 근육 타입입니다."),

    ROUTINE_TYPE_INVALID("ROUTINE_TYPE_001", "유효하지 않은 루틴 타입입니다."),

    // 유저(User) 관련 에러 코드
    USER_NOT_FOUND("USER_001", "존재하지 않는 유저입니다."),

    // 식단(Diet) 관련 에러 코드
    DIET_NOT_FOUND("DIET_001", "존재하지 않는 식단입니다."),

    // 음식(Food) 관련 에러 코드
    FOOD_NOT_FOUND("FOOD_001", "존재하지 않는 음식입니다."),

    // 식단 음식(DietFood) 관련 에러 코드
    DIET_FOOD_NOT_FOUND("DIET_FOOD_001", "존재하지 않는 식단 음식입니다."),

    // 인증 에러코드
    AUTH_UNAUTHORIZED("AUTH_001", "인증이 필요합니다."),

    // 다른 사용자의 데이터 접근 에러코드
    AUTH_FORBIDDEN("AUTH_002", "권한이 없습니다."),

    // 이미 존재하는 유저,
    USER_ALREADY_EXISTS("AUTH_003", "이미 존재하는 유저입니다."),

    // 만료/유효하지 않은 토큰
    AUTH_INVALID("AUTH_004", "토큰이 유효하지 않습니다."),

    // 파싱오류(Bearer 없음)
    AUTH_MISSING_BEARER("AUTH_005", "Bearer 키워드를 확인하세요."),

    // 파싱오류(Bearer 없음)
    AUTH_WRONG_TOKEN_TYPE("AUTH_006", "토큰 타입이 올바르지 않습니다."),

    // 약관 미동의
    REQUIRED_TERMS_NOT_AGREED("TERM_001", "약관에 동의하지 않았습니다."),

    // 잘못된 요청 본문(JSON 파싱 실패 등)
    BAD_REQUEST("REQ_400", "잘못된 요청입니다."),

    // 루틴(Routine) 관련 에러 코드
    ROUTINE_NOT_FOUND("ROUTINE_001", "존재하지 않는 루틴입니다."),

    // 운동(Exercise) 관련 에러 코드,
    EXERCISE_NOT_FOUND("EXERCISE_001", "존재하지 않는 운동입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
