package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "루틴 세부 정보 생성 요청 DTO")
public record RoutineDetailCreateRequest(
    @Schema(description = "운동 시간 (단위: 분)", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 0, message = "시간은 0 이상이어야 합니다.")
    Integer time,

    @Schema(description = "운동 강도 (가능한 값: 느슨함, 보통, 강함)", example = "느슨함", requiredMode = Schema.RequiredMode.REQUIRED)
    String intensity
) {}
