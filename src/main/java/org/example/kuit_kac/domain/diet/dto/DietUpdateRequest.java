package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import org.example.kuit_kac.domain.diet_food.dto.DietFoodUpdateRequest;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "식단 수정 요청 DTO입니다.")
public class DietUpdateRequest {

    @Schema(description = "식단 유형", example = "아침", allowableValues = {"아침", "점심", "저녁", "간식", "단식", "나만의 식단"})
    private String dietType;

    @Schema(description = "식단 기록 유형", example = "기록", allowableValues = {"기록", "계획", "AI 계획", "외식", "술자리"})
    private String dietEntryType;

    @Schema(description = "식단 시간", example = "2025-07-10T08:00:00")
    private LocalDateTime dietTime;

    @Schema(description = "해당 식단에 포함될 음식 목록")
    private List<DietFoodUpdateRequest> foods;
}