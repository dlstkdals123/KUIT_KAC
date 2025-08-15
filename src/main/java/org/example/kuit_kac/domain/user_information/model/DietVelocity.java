package org.example.kuit_kac.domain.user_information.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DietVelocity {
    YUMYUM("YUMYUM", "냠냠모드"),
    COACH("COACH", "코치모드"),
    ALL_IN("ALL_IN", "올인모드");

    private final String value;
    private final String koreanName;
}
