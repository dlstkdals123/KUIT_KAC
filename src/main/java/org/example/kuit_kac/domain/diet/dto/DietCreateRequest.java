package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "새로운 끼니 생성 요청 DTO입니다. 해당 끼니에 포함될 음식 정보를 포함합니다.")
public class DietCreateRequest {

    @NotNull(message = "끼니 이름은 필수입니다.")
    @Schema(description = "끼니 이름", example = "오늘 점심", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "끼니 유형은 필수입니다.")
    @Schema(description = "끼니 유형", example = "LUNCH", allowableValues = {"BREAKFAST", "LUNCH", "DINNER", "SNACK", "TEMPLATE"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private DietType dietType;

    @NotNull(message = "끼니 기록 유형은 필수입니다.")
    @Schema(description = "끼니 기록 유형", example = "RECORD", allowableValues = {"RECORD", "PLAN", "AI_PLAN", "DINING_OUT", "DRINKING"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private DietEntryType dietEntryType;

    @Schema(description = "끼니 시간은 끼니 유형이 TEMPLATE이 아닌 경우 필수입니다. 끼니 시간 (YYYY-MM-DDTHH:MM:SS 형식)", example = "2025-07-12T19:00:00")
    private LocalDateTime dietTime;

    @NotEmpty(message = "끼니에는 최소 하나 이상의 음식이 포함되어야 합니다.")
    @Schema(description = "해당 끼니에 포함될 음식 목록")
    private List<DietFoodCreateRequest> dietFoods;
}