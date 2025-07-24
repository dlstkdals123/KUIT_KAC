package org.example.kuit_kac.domain.exercise.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.global.util.EnumConverter;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum TargetMuscleGroup {
    ABDOMINALS("Abdominals", "복근"),
    ABDUCTORS("Abductors", "외전근"),
    ADDUCTORS("Adductors", "내전근"),
    BACK("Back", "등"),
    BICEPS("Biceps", "이두근"),
    CALVES("Calves", "종아리"),
    CHEST("Chest", "가슴"),
    FOREARMS("Forearms", "전완근"),
    GLUTES("Glutes", "둔근"),
    HAMSTRINGS("Hamstrings", "햄스트링"),
    HIP_FLEXORS("Hip Flexors", "고관절 굴곡근"),
    QUADRICEPS("Quadriceps", "대퇴사두근"),
    SHINS("Shins", "정강이"),
    SHOULDERS("Shoulders", "어깨"),
    TRAPEZIUS("Trapezius", "승모근"),
    TRICEPS("Triceps", "삼두근");

    private final String value;
    private final String koreanName;

    public static TargetMuscleGroup getTargetMuscleGroup(String targetMuscleGroup) {
        TargetMuscleGroup fromKorean = EnumConverter.fromKoreanTargetMuscleGroup(targetMuscleGroup);
        if (fromKorean != null) return fromKorean;
        try {
            return TargetMuscleGroup.valueOf(targetMuscleGroup.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TARGET_MUSCLE_GROUP_INVALID);
        }
    }
}
