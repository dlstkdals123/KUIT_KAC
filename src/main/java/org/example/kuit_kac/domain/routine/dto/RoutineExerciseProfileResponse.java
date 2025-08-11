package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;

@Schema(description = "루틴에 포함된 운동 상세 정보 DTO")
public record RoutineExerciseProfileResponse(
    @Schema(description = "루틴운동 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "운동 정보") 
    ExerciseProfileResponse exercise
) {
    public static RoutineExerciseProfileResponse from(RoutineExercise routineExercise) {
        return new RoutineExerciseProfileResponse(
                routineExercise.getId(),
                ExerciseProfileResponse.from(routineExercise.getExercise())
        );
    }
} 