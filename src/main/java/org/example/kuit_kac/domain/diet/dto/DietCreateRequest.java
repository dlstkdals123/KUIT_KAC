package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.meal.dto.MealCreateRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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

    @NotNull(message = "식단 유형은 필수입니다.")
    @Schema(description = "식단 유형", example = "RECORD", allowableValues = {"RECORD", "PLAN", "AI_PLAN", "FASTING", "DINING_OUT", "DRINKING"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private DietType dietType;

    @NotNull(message = "식단 날짜는 필수입니다.")
    @Schema(description = "식단 날짜 (YYYY-MM-DD 형식)", example = "2025-07-12", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dietDate;

    @Schema(description = "해당 식단에 포함될 끼니 목록 (FASTING 유형의 경우 비어있거나 생략)")
    private List<MealCreateRequest> meals;
}