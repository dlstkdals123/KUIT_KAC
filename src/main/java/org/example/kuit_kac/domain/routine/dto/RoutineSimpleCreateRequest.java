package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "운동 기록 생성 요청 DTO")
public record RoutineSimpleCreateRequest(
    @Schema(description = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유저 ID는 필수입니다.")
    Long userId,

    @Schema(description = "유산소 운동 디테일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유산소 운동 디테일은 필수입니다.")
    RoutineDetailCreateRequest aerobicDetail,

    @Schema(description = "무산소 운동 디테일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "무산소 운동 디테일은 필수입니다.")
    RoutineDetailCreateRequest anaerobicDetail
) {} 