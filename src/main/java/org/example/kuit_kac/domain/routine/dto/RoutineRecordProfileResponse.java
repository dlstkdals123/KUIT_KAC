package org.example.kuit_kac.domain.routine.dto;

import org.example.kuit_kac.domain.routine.model.Routine;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "루틴 기록 응답 DTO")
public record RoutineRecordProfileResponse(
    @Schema(description = "루틴의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "나만의 루틴 이름", example = "오늘의 상체 루틴", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "루틴의 유형 (가능한 값: 기록, 나만의 루틴)", example = "기록", requiredMode = Schema.RequiredMode.REQUIRED)
    String routineType,

    @Schema(description = "루틴 정보 생성일시 (형식: YYYY-MM-DDTHH:MM:SS)", example = "2025-07-10T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "루틴 정보 최종 수정일시 (형식: YYYY-MM-DDTHH:MM:SS)", example = "2025-07-10T12:00:00")
    LocalDateTime updatedAt,

    @Schema(description = "루틴에 포함된 운동 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<RoutineExerciseProfileResponse> routineExerciseProfiles
) {

    public static RoutineRecordProfileResponse from(Routine routine) {
        List<RoutineExerciseProfileResponse> routineExerciseProfiles = routine.getRoutineExercises().stream()
                .map(RoutineExerciseProfileResponse::from)
                .toList();

        return from(routineExerciseProfiles, routine);
    }

    private static RoutineRecordProfileResponse from(List<RoutineExerciseProfileResponse> routineExerciseProfiles, Routine routine) {
        return new RoutineRecordProfileResponse(
                routine.getId(),
                routine.getName(),
                routine.getRoutineType() != null ? routine.getRoutineType().getKoreanName() : null,
                routine.getCreatedAt(),
                routine.getUpdatedAt(),
                routineExerciseProfiles
        );
    }
}
