package org.example.kuit_kac.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
@Getter
public class OnboardingAlreadyDoneException extends RuntimeException {
    private final Long userId;
    public OnboardingAlreadyDoneException(Long userId) {
        super("이미 온보딩 완료된 유저입니다.");
        this.userId = userId;
    }
}
