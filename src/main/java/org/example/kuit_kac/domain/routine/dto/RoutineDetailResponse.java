package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.routine.model.RoutineDetail;

import java.time.LocalDateTime;

@Schema(description = "루틴 상세 정보 응답 DTO")
public record RoutineDetailResponse(
    @Schema(description = "루틴 상세의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "루틴 운동 정보")
    RoutineExerciseProfileResponse routineExercise,

    @Schema(description = "운동 시간 (분)", example = "30")
    Integer time,

    @Schema(description = "운동 강도", example = "보통")
    String intensity,

    @Schema(description = "루틴 상세 정보 생성일시", example = "2025-07-24T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "루틴 상세 정보 최종 수정일시", example = "2025-07-24T12:00:00")
    LocalDateTime updatedAt
) {
    public static RoutineDetailResponse from(RoutineDetail routineDetail) {
        return new RoutineDetailResponse(
                routineDetail.getId(),
                RoutineExerciseProfileResponse.from(routineDetail.getRoutineExercise()),
                routineDetail.getTime(),
                routineDetail.getIntensity() != null ? routineDetail.getIntensity().getKoreanName() : null,
                routineDetail.getCreatedAt(),
                routineDetail.getUpdatedAt()
        );
    }
}
