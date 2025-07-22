package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import java.util.List;

@Schema(description = "식단 생성 요청 DTO")
public record DietCreateRequest(
    @Schema(description = "식단을 생성할 사용자 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long userId,

    @Schema(description = "식단 이름", example = "점심식단", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(description = "식단 유형(BREAKFAST, LUNCH, DINNER, SNACK, TEMPLATE)", example = "LUNCH", requiredMode = Schema.RequiredMode.REQUIRED)
    String dietType,

    @Schema(description = "식단 항목 종류(RECORD, FASTING, PLAN, AI_PLAN, DINING_OUT, DRINKING)", example = "RECORD", requiredMode = Schema.RequiredMode.REQUIRED)
    String dietEntryType,

    @Schema(description = "식단에 포함될 음식 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<DietFoodCreateRequest> foods
) {}