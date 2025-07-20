package org.example.kuit_kac.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    MALE("MALE", "남성"),
    FEMALE("FEMALE", "여성");

    private final String value;
    private final String koreanName;
}
