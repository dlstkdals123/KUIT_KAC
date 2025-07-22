package org.example.kuit_kac.domain.diet_food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Schema(description = "식단 음식 수정 요청 DTO")
public record DietFoodUpdateRequest(
    @Schema(description = "음식 ID", example = "10")
    @NotNull(message = "음식 ID는 필수입니다.")
    Long foodId,

    @Schema(description = "음식 섭취량", example = "1.0")
    @Positive(message = "음식 섭취량은 0보다 커야 합니다.")
    @DecimalMin(value = "0.1", message = "음식 섭취량은 0.1 이상이어야 합니다.")
    double quantity,

    @Schema(description = "음식 섭취 시간", example = "2024-06-01T12:00:00")
    @NotNull(message = "음식 섭취 시간은 필수입니다.")
    LocalDateTime dietTime
) {} 