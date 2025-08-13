package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.routine.model.RoutineSet;

import java.time.LocalDateTime;

@Schema(description = "루틴 세트 정보 응답 DTO")
public record RoutineSetResponse(
    @Schema(description = "루틴 세트의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "루틴 운동 정보")
    RoutineExerciseProfileResponse routineExercise,

    @Schema(description = "반복 횟수", example = "10")
    Integer count,

    @Schema(description = "중량 (kg)", example = "50")
    Integer weightKg,

    @Schema(description = "중량 개수", example = "2")
    Integer weightNum,

    @Schema(description = "거리 (m)", example = "1000")
    Integer distance,

    @Schema(description = "시간 (분)", example = "15.5")
    Double time,

    @Schema(description = "세트 순서", example = "1")
    Integer setOrder,

    @Schema(description = "루틴 세트 정보 생성일시", example = "2025-07-24T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "루틴 세트 정보 최종 수정일시", example = "2025-07-24T12:00:00")
    LocalDateTime updatedAt
) {
    public static RoutineSetResponse from(RoutineSet routineSet) {
        return new RoutineSetResponse(
                routineSet.getId(),
                RoutineExerciseProfileResponse.from(routineSet.getRoutineExercise()),
                routineSet.getCount(),
                routineSet.getWeightKg(),
                routineSet.getWeightNum(),
                routineSet.getDistance(),
                routineSet.getTime(),
                routineSet.getSetOrder(),
                routineSet.getCreatedAt(),
                routineSet.getUpdatedAt()
        );
    }
}
