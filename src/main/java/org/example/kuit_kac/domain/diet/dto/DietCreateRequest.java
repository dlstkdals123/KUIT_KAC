package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.global.util.EnumConverter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "새로운 식단 기록 생성 요청 DTO입니다. 식단, 끼니, 음식 정보를 포함합니다.")
public class DietCreateRequest {

    @NotNull(message = "사용자 ID는 필수입니다.")
    @Schema(description = "식단 기록을 생성할 사용자의 고유 ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "식단 이름은 필수입니다.")
    @Schema(description = "식단 이름", example = "오늘의 아침", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "식단 유형은 필수입니다.")
    @Schema(description = "식단 유형", example = "아침", allowableValues = {"아침", "점심", "저녁", "간식", "단식", "나만의 식단"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String dietType;

    @Schema(description = "식단 기록 유형", example = "기록", allowableValues = {"기록", "계획", "AI 계획", "외식", "술자리"})
    private String dietEntryType;

    @NotNull(message = "식단 시간은 필수입니다.")
    @Schema(description = "식단 시간", example = "2025-07-12T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dietTime;

    @Schema(description = "해당 식단에 포함될 끼니 목록 (FASTING 유형의 경우 비어있거나 생략)")
    private List<DietFoodCreateRequest> dietFoods;

    // 한국어를 영어로 변환하는 메서드들
    public DietType getDietTypeEnum() {
        return EnumConverter.fromKoreanDietType(this.dietType);
    }

    public DietEntryType getDietEntryTypeEnum() {
        return this.dietEntryType != null ? EnumConverter.fromKoreanDietEntryType(this.dietEntryType) : null;
    }
}