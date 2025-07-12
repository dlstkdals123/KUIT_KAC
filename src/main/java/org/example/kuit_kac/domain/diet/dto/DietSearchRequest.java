package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Parameter(description = "식단 기록을 조회할 사용자의 고유 ID", example = "101", required = true)
    private Long userId;

    @Parameter(description = "조회할 식단 유형 (예: RECORD, PLAN, AI_PLAN, FASTING). 선택 사항입니다.", example = "RECORD")
    private DietType dietType;

    @Parameter(description = "조회할 날짜 (YYYY-MM-DD 형식). 필수 항목입니다.", example = "2025-07-12", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dietDate;
}