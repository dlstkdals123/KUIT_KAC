package org.example.kuit_kac.domain.diet.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "식단 기록 조회를 위한 검색 조건 DTO")
public class DietSearchRequest {
    @NotNull(message = "사용자 ID는 필수입니다.")
    @Parameter(description = "식단 기록을 조회할 사용자의 고유 ID.", example = "101")
    private Long userId;

    @NotNull(message = "식단 유형은 필수입니다.")
    @Parameter(description = "조회할 식단 유형 (예: 아침, 점심, 저녁, 간식, 단식, 나만의 식단).", example = "아침")
    private String dietType;
}
