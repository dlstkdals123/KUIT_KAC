package org.example.kuit_kac.domain.user_information.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppetiteType {
    SMALL("SMALL", "식욕적음"),
    BIG("BIG", "식욕많음");

    private final String value;
    private final String koreanName;
}
