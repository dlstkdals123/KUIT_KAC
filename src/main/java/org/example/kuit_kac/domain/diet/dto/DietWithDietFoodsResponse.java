package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Schema(description = "식단과 해당 식단에 포함된 음식 목록 정보를 담는 응답 DTO입니다.")
public class DietWithDietFoodsResponse {
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

    @Schema(description = "해당 식단에 포함되는 음식 목록 (DietFood 정보 포함)")
    private List<DietFoodResponse> foodItems;

    @Schema(description = "해당 식단에 포함되는 AI 음식 목록 (DietAifood 정보 포함)")
    private List<DietFoodResponse> aifoodItems;

    public static DietWithDietFoodsResponse from(Diet diet) {
        List<DietFoodResponse> foodItems = diet.getDietFoods().stream()
                .map(DietFoodResponse::from)
                .collect(Collectors.toList());

        List<DietFoodResponse> aifoodItems = diet.getDietAifoods().stream()
                .map(DietFoodResponse::from)
                .collect(Collectors.toList());

        return new DietWithDietFoodsResponse(
                diet.getId(),
                diet.getUser().getId(),
                diet.getName(),
                diet.getDietType().getKoreanName(),
                diet.getDietEntryType().getKoreanName(),
                diet.getDietTime(),
                diet.getCreatedAt(),
                diet.getUpdatedAt(),
                foodItems,
                aifoodItems
        );
    }
}