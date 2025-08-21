package org.example.kuit_kac.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomException extends ception {

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
