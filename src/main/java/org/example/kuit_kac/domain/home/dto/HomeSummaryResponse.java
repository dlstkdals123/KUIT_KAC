package org.example.kuit_kac.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.food.model.Food;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "홈화면 응답 DTO")
public class HomeSummaryResponse {
    // 일일섭취목표, 오늘 먹은 음식들의 칼로리, 현재 체중, 남은 칼로리
    @Schema(description = "일일 섭취 목표 칼로리")
    private double dailyKCalorieGoal;

    @Schema(description = "오늘 섭취한 총 칼로리")
    private double totalIntakeKCalorie;

    @Schema(description = "현재 체중")
    private double weight;

    @Schema(description = "오늘 섭취가능한 남은 칼로리")
    private double remainingKCalorie;
}
