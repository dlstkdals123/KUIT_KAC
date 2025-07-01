package org.example.kuit_kac.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final LocalDateTime timestamp;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "CustomException{" +
                errorCode.toString() +
                ", timestamp=" + timestamp +
                '}';
    }
}
