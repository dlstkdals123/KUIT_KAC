package org.example.kuit_kac.domain.home.model;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietVelocity {
    YUMYUM(84), // 12주
    COACH(56), // 8주
    ALL_IN(28); // 4주

    private final int periodInDays;
}
