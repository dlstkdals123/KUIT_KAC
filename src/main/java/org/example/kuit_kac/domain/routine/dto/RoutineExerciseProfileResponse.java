package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;

import java.util.List;

@Schema(description = "루틴에 포함된 운동 상세 정보 DTO")
public record RoutineExerciseProfileResponse(
    @Schema(description = "루틴운동 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "운동 정보") 
    ExerciseProfileResponse exercise,

    @Schema(description = "루틴 상세 정보 (OneToOne)")
    RoutineDetailResponse routineDetail,

    @Schema(description = "루틴 세트 목록 (OneToMany)")
    List<RoutineSetResponse> routineSets
) {
    public static RoutineExerciseProfileResponse from(RoutineExercise routineExercise) {
        return new RoutineExerciseProfileResponse(
                routineExercise.getId(),
                ExerciseProfileResponse.from(routineExercise.getExercise()),
                routineExercise.getRoutineDetail() != null ? 
                    RoutineDetailResponse.from(routineExercise.getRoutineDetail()) : null,
                routineExercise.getRoutineSets() != null ? 
                    routineExercise.getRoutineSets().stream()
                        .map(RoutineSetResponse::from)
                        .toList() : List.of()
        );
    }
} 