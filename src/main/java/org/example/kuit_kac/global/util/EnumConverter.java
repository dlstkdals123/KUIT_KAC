package org.example.kuit_kac.global.util;

import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.food.model.FoodType;
import org.example.kuit_kac.domain.routine.model.Intensity;
import org.example.kuit_kac.domain.routine.model.RoutineType;
import org.example.kuit_kac.domain.routine.model.TargetMuscleGroup;
import org.example.kuit_kac.domain.user.model.GenderType;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumConverter {

    // GenderType 변환
    private static final Map<String, GenderType> GENDER_TYPE_MAP = Arrays.stream(GenderType.values())
            .collect(Collectors.toMap(GenderType::getKoreanName, genderType -> genderType));

    public static GenderType fromKoreanGender(String koreanName) {
        return GENDER_TYPE_MAP.get(koreanName);
    }

    // DietType 변환
    private static final Map<String, DietType> DIET_TYPE_MAP = Arrays.stream(DietType.values())
            .collect(Collectors.toMap(DietType::getKoreanName, dietType -> dietType));

    public static DietType fromKoreanDietType(String koreanName) {
        return DIET_TYPE_MAP.get(koreanName);
    }

    // DietEntryType 변환
    private static final Map<String, DietEntryType> DIET_ENTRY_TYPE_MAP = Arrays.stream(DietEntryType.values())
            .collect(Collectors.toMap(DietEntryType::getKoreanName, dietEntryType -> dietEntryType));

    public static DietEntryType fromKoreanDietEntryType(String koreanName) {
        return DIET_ENTRY_TYPE_MAP.get(koreanName);
    }

    // FoodType 변환
    private static final Map<String, FoodType> FOOD_TYPE_MAP = Arrays.stream(FoodType.values())
            .collect(Collectors.toMap(FoodType::getKoreanName, foodType -> foodType));

    public static FoodType fromKoreanFoodType(String koreanName) {
        return FOOD_TYPE_MAP.get(koreanName);
    }

    // Intensity 변환
    private static final Map<String, Intensity> INTENSITY_MAP = Arrays.stream(Intensity.values())
            .collect(Collectors.toMap(Intensity::getKoreanName, intensity -> intensity));

    public static Intensity fromKoreanIntensity(String koreanName) {
        return INTENSITY_MAP.get(koreanName);
    }

    // TargetMuscleGroup 변환
    private static final Map<String, TargetMuscleGroup> TARGET_MUSCLE_GROUP_MAP = Arrays.stream(TargetMuscleGroup.values())
            .collect(Collectors.toMap(TargetMuscleGroup::getKoreanName, targetMuscleGroup -> targetMuscleGroup));

    public static TargetMuscleGroup fromKoreanTargetMuscleGroup(String koreanName) {
        return TARGET_MUSCLE_GROUP_MAP.get(koreanName);
    }

    // RoutineType 변환
    private static final Map<String, RoutineType> ROUTINE_TYPE_MAP = Arrays.stream(RoutineType.values())
            .collect(Collectors.toMap(RoutineType::getKoreanName, routineType -> routineType));

    public static RoutineType fromKoreanRoutineType(String koreanName) {
        return ROUTINE_TYPE_MAP.get(koreanName);
    }
} 