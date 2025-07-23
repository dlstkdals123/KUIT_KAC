package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "운동 강도")
public enum Intensity {
    @Schema(description = "느슨함")
    LOOSE,
    @Schema(description = "보통")
    NORMAL,
    @Schema(description = "강함")
    TIGHT
}
