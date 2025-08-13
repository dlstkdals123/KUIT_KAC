package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "루틴에 포함될 운동 생성 요청 DTO")
public record RoutineExerciseCreateRequest(
    //TODO: 하나의 요청에 운동 ID가 중복되어서는 안됩니다.
    @Schema(description = "운동 ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "운동 ID는 필수입니다.")
    Long exerciseId,

    @Schema(description = "루틴 디테일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "루틴 디테일은 필수입니다.")
    @Valid
    RoutineDetailCreateRequest routineDetail,

    @Schema(description = "루틴 세트 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "루틴 세트 목록은 필수입니다.")
    @Size(min = 1, message = "루틴 세트 목록은 한 개 이상 등록해야 합니다.")
    List<@Valid RoutineSetCreateRequest> routineSets
) {} 