package org.example.kuit_kac.domain.user_information.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Activity {
    VERY_LOW("VERY_LOW", "매우 낮은 활동량"),
    LOW("LOW", "낮은 활동량(주 1~2회)"),
    NORMAL("NORMAL", "보통 활동량(주 3~4회)"),
    HIGH("HIGH", "높은 활동량(주 5회 이상)"),
    VERY_HIGH("VERY_HIGH", "매우 높은 활동량(매일)");

    private final String value;
    private final String koreanName;
}
