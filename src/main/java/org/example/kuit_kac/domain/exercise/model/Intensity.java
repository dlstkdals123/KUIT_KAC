package org.example.kuit_kac.domain.exercise.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.global.util.EnumConverter;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum Intensity {
    LOOSE("LOOSE", "느슨함"),
    NORMAL("NORMAL", "보통"),
    TIGHT("TIGHT", "강함");

    private final String value;
    private final String koreanName;

    public static Intensity getIntensity(String intensity) {
        Intensity fromKorean = EnumConverter.fromKoreanIntensity(intensity);
        if (fromKorean != null) return fromKorean;
        try {
            return Intensity.valueOf(intensity.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INTENSITY_INVALID);
        }
    }
}