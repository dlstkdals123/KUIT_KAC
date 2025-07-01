package org.example.kuit_kac.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
