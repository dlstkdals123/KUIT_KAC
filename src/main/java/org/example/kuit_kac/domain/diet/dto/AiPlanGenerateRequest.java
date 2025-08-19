package org.example.kuit_kac.domain.diet.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "AI 계획 생성 요청 DTO")
public record AiPlanGenerateRequest(
    @Schema(description = "외식/술자리 활동 목록 (1~4개)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "외식/술자리 활동 목록은 필수입니다.")
    @Size(min = 1, message = "외식/술자리 활동은 한 개 이상 등록해야 합니다.")
    @Size(max = 4, message = "외식/술자리 활동은 최대 4개까지 등록할 수 있습니다.")
    List<DietActivityCreateRequest> dietActivities
) {} 