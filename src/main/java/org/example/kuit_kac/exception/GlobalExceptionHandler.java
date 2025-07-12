package org.example.kuit_kac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus httpStatus = convertErrorCodeToHttpStatus(errorCode);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", httpStatus.value());
        errorDetails.put("error", httpStatus.getReasonPhrase());
        errorDetails.put("code", errorCode.getCode());
        errorDetails.put("message", errorCode.getMessage());

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    private HttpStatus convertErrorCodeToHttpStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case USER_NOT_FOUND:
            case DIET_USER_ID_NOT_FOUND:
            case DIET_USER_ID_AND_DIET_TYPE_NOT_FOUND:
            case DIET_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND:
            case MEAL_DIET_NOT_FOUND:
            case FOOD_NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case DIET_TYPE_INVALID:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}