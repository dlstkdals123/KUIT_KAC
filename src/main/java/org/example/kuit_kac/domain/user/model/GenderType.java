package org.example.kuit_kac.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    MALE("male"),
    FEMALE("female");

    private final String value;
}
