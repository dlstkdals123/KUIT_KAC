package org.example.kuit_kac.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "체중 등록/수정 요청에 필요한 DTO")
public class HomeWeightRequest {
    @Schema(description = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @Schema(description = "체중")
    @Positive(message = "체중은 0.0보다 커야 합니다.")
    private double weight;
}
