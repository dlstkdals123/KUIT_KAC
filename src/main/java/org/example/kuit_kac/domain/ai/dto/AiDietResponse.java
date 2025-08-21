package org.example.kuit_kac.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodProfileResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Schema(description = "AI 식단 생성 응답 (DietRecordProfileResponse 형태)")
public record AiDietResponse(
    @Schema(description = "생성된 식단 데이터", example = "{\"2025-08-11\": [{\"id\": 1, \"name\": \"아침\", \"dietDate\": \"2025-08-11\", \"dietType\": \"아침\", \"dietEntryType\": \"AI 계획\", \"foodStatusType\": \"양호\", \"createdAt\": \"2025-08-11T08:00:00\", \"updatedAt\": \"2025-08-11T08:00:00\", \"totalKcal\": 450.0, \"dietFoods\": []}]}")
    @JsonProperty("response")
    Map<String, List<DietRecordProfileResponse>> response
) {
    @Schema(description = "식단 기록 응답 DTO")
    public record DietRecordProfileResponse(
        @Schema(description = "식단의 고유 식별자 (ID)", example = "1")
        Long id,

        @Schema(description = "식단의 이름", example = "아침")
        String name,

        @Schema(description = "식단의 날짜 (형식: YYYY-MM-DD)", example = "2025-08-11")
        LocalDate dietDate,

        @Schema(description = "식단의 유형 (아침, 점심, 저녁, 간식, 나만의 식단)", example = "아침")
        String dietType,

        @Schema(description = "식단의 항목 종류 (기록, 단식, 계획, AI 계획, 외식, 술자리)", example = "AI 계획")
        String dietEntryType,

        @Schema(description = "식단의 상태 (양호, 적당, 위험)", example = "양호")
        String foodStatusType,

        @Schema(description = "식단 정보 생성일시 (형식: YYYY-MM-DDTHH:MM:SS)")
        String createdAt,

        @Schema(description = "식단 정보 최종 수정일시 (형식: YYYY-MM-DDTHH:MM:SS)")
        String updatedAt,

        @Schema(description = "식단의 총 칼로리 (단위: kcal)", example = "450.0")
        Double totalKcal,

        @Schema(description = "식단에 포함된 음식 목록")
        List<DietFoodProfileResponse> dietFoods
    ) {}
}
