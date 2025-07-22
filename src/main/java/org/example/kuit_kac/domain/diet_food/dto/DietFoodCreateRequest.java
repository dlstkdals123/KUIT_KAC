package org.example.kuit_kac.domain.diet_food.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "식단에 포함될 음식 생성 요청 DTO")
public record DietFoodCreateRequest(
    @Schema(description = "음식 ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    Long foodId,

    @Schema(description = "음식 섭취량", example = "1.0", requiredMode = Schema.RequiredMode.REQUIRED)
    double quantity,

    @Schema(description = "음식 섭취 시간(ISO-8601)", example = "2024-06-01T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime dietTime
) {} 