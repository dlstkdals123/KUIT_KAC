package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "루틴 타입")
public enum RoutineType {
    @Schema(description = "기록 루틴")
    RECORD,
    @Schema(description = "템플릿 루틴")
    TEMPLATE
}
