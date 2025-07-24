package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "루틴에 포함될 운동 생성 요청 DTO")
public record RoutineExerciseCreateRequest(
    //TODO: 하나의 요청에 운동 ID가 중복되어서는 안됩니다.
    @Schema(description = "운동 ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "운동 ID는 필수입니다.")
    Long exerciseId
) {} 