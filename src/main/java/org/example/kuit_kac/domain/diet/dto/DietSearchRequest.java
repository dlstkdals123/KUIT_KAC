package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "식단 기록 조회를 위한 검색 조건 DTO")
public class DietSearchRequest {

    @NotNull(message = "사용자 ID는 필수이며, 공백일 수 없습니다.")
    @Schema(description = "식단 기록을 조회할 사용자의 고유 ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "식단 유형은 필수이며, 공백일 수 없습니다.")
    @Schema(description = "식단 유형", example = "RECORD", allowableValues = {"RECORD", "PLAN", "AI_PLAN", "FASTING", "DINING_OUT", "DRINKING"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private DietType dietType;

    @NotNull(message = "날짜는 필수이며, 공백일 수 없습니다.")
    @Schema(description = "조회할 날짜 (YYYY-MM-DD 형식).", example = "2025-07-12", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dietDate;
}