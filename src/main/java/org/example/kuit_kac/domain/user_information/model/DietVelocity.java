package org.example.kuit_kac.domain.user_information.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietVelocity {
    YUMYUM("YUMYUM", "냠냠모드", 84), // 12주
    COACH("COACH", "코치모드", 56), // 8주
    ALL_IN("ALL_IN", "올인모드", 28); // 4주

    private final String value;
    private final String koreanName;
    private final int periodInDays;

}
