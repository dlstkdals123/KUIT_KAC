package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "루틴 세트 생성 요청 DTO")
public record RoutineSetCreateRequest(
    @Schema(description = "반복 횟수 (단위: 회)", example = "15")
    @Min(value = 0, message = "반복 횟수는 0 이상이어야 합니다.")
    Integer count,

    @Schema(description = "중량 (단위: kg)", example = "20")
    @Min(value = 0, message = "중량은 0 이상이어야 합니다.")
    Integer weightKg,

    @Schema(description = "중량 개수 (단위: 개)", example = "2")
    @Min(value = 0, message = "중량 개수는 0 이상이어야 합니다.")
    Integer weightNum,

    @Schema(description = "거리 (단위: m)", example = "1000")
    @Min(value = 0, message = "거리는 0 이상이어야 합니다.")
    Integer distance,

    @Schema(description = "시간 (단위: 분)", example = "5.5")
    @Min(value = 0, message = "시간은 0 이상이어야 합니다.")
    Double time,

    @Schema(description = "세트 순서 (단위: 순서)", example = "1")
    @Min(value = 0, message = "세트 순서는 0 이상이어야 합니다.")
    Integer setOrder
) {}
