package org.example.kuit_kac.domain.routine.model;

import org.example.kuit_kac.global.util.EnumConverter;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoutineType {
    RECORD("RECORD", "기록"),
    TEMPLATE("TEMPLATE", "나만의 루틴");

    private final String value;
    private final String koreanName;

    public static RoutineType getRoutineType(String routineType) {
        RoutineType fromKorean = EnumConverter.fromKoreanRoutineType(routineType);
        if (fromKorean != null) return fromKorean;
        try {
            return RoutineType.valueOf(routineType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.ROUTINE_TYPE_INVALID);
        }
    }
} 