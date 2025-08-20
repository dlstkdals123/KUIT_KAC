package org.example.kuit_kac.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "계획 식단 생성 요청 DTO")
public record DietAiPlanTotalCreateRequestAndResponse(
    @Schema(description = "AI 계획 생성 요청 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid AiPlanGenerateRequest dietAiPlanGenerateRequest,

    @Schema(description = "계획 식단 날짜 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "계획 식단 날짜 목록은 필수입니다.")
    @Size(min = 1, message = "계획 식단 날짜는 한 개 이상 등록해야 합니다.")
    List<@Valid DietAiPlanCreateRequest> plans
) {
    public static DietAiPlanTotalCreateRequestAndResponse from(AiPlanGenerateRequest dietAiPlanGenerateRequest, List<DietAiPlanCreateRequest> plans) {
        return new DietAiPlanTotalCreateRequestAndResponse(
            dietAiPlanGenerateRequest,
            plans
        );
    }
} 