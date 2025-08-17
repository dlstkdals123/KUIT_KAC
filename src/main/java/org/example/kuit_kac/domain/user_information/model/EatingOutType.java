package org.example.kuit_kac.domain.user_information.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EatingOutType {
    FASTFOOD("FASTFOOD", "패스트푸드"),
    KOREAN("KOREAN", "한식"),
    CHINESE("CHINESE", "중식"),
    WESTERN("WESTERN", "양식"),
    ASIAN("ASIAN", "아시안"),
    FRIED("FRIED", "튀김");

    private final String value;
    private final String koreanName;
}
