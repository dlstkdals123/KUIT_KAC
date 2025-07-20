package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import org.example.kuit_kac.domain.diet.model.Diet;

@Getter
@AllArgsConstructor
@Schema(description = "식단 정보를 담는 응답 DTO입니다. 식단 조회, 등록 등의 API 응답에 사용됩니다.")
public class DietResponse {
    @Schema(description = "식단의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "연결된 사용자의 고유 식별자 (User ID)", example = "101")
    private Long userId;

    @Schema(description = "식단 이름", example = "오늘의 아침")
    private String name;

    @Schema(description = "식단 유형", example = "아침", allowableValues = {"아침", "점심", "저녁", "간식", "단식", "나만의 식단"})
    private String dietType;

    @Schema(description = "식단 기록 유형", example = "기록", allowableValues = {"기록", "계획", "AI 계획", "외식", "술자리"})
    private String dietEntryType;

    @Schema(description = "식단 시간", example = "2025-07-10T08:00:00")
    private LocalDateTime dietTime;

    @Schema(description = "식단 정보 생성일시", example = "2025-07-10T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "식단 정보 최종 수정일시", example = "2025-07-10T12:00:00")
    private LocalDateTime updatedAt;

    public static DietResponse from(Diet diet) {
        return new DietResponse(
                diet.getId(),
                diet.getUser().getId(),
                diet.getName(),
                diet.getDietType().getKoreanName(),
                diet.getDietEntryType().getKoreanName(),
                diet.getDietTime(),
                diet.getCreatedAt(),
                diet.getUpdatedAt()
        );
    }
}