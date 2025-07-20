package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "끼니 정보 전체 업데이트를 위한 요청 DTO입니다. 끼니의 모든 필드와 연결된 음식 목록을 포함합니다.")
public class DietUpdateRequest {

    @NotNull(message = "끼니 유형은 필수입니다.")
    @Schema(description = "끼니 유형", example = "LUNCH", allowableValues = {"BREAKFAST", "LUNCH", "DINNER", "SNACK"})
    private DietType dietType;

    @NotNull(message = "끼니를 섭취한 시간은 필수입니다.")
    @Schema(description = "끼니를 섭취한 시간", example = "2025-07-10T12:30:00")
    private LocalDateTime dietTime;

    @NotEmpty(message = "끼니에 포함될 음식 목록은 필수입니다.")
    @Schema(description = "끼니에 포함될 음식 목록입니다. 기존 음식 정보는 모두 이 목록으로 대체됩니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DietFoodUpdateRequest> foods;
}