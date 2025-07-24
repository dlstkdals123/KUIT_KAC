package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "단식/외식/술자리 식단 생성 요청 DTO")
public record DietSimpleCreateRequest(
    @Schema(description = "유저 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "유저 ID는 필수입니다.")
    Long userId,

    @Schema(description = "식단 항목 종류(단식, 외식, 술자리)", example = "단식", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식단 항목 종류는 필수입니다.")
    String dietEntryType,

    @Schema(description = "식단 종류", example = "아침", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "식단 종류는 필수입니다.")
    String dietType
) {} 