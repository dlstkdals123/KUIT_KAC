package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "식단 정보를 담는 응답 DTO입니다. 식단 조회, 등록 등의 API 응답에 사용됩니다.")
public class DietResponse {
    @Schema(description = "식단의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "연결된 사용자의 고유 식별자 (User ID)", example = "101")
    private Long userId;

    @Schema(description = "식단 유형", example = "RECORD", allowableValues = {"RECORD", "PLAN", "AI_PLAN", "FASTING", "DINING_OUT", "DRINKING"})
    private DietType dietType;

    @Schema(description = "식단 날짜", example = "2025-07-12")
    private LocalDate dietDate;

    @Schema(description = "식단 정보 생성일시", example = "2025-07-12T09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "식단 정보 최종 수정일시", example = "2025-07-12T09:00:00")
    private LocalDateTime updatedAt;

    public static DietResponse from(Diet diet) {
        return new DietResponse(
                diet.getId(),
                diet.getUser().getId(),
                diet.getDietType(),
                diet.getDietDate(),
                diet.getCreatedAt(),
                diet.getUpdatedAt()
        );
    }
}