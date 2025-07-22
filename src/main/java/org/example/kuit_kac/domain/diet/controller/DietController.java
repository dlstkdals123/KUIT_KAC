package org.example.kuit_kac.domain.diet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.dto.*;
import org.example.kuit_kac.domain.diet.model.*;
import org.example.kuit_kac.domain.diet.service.*;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.example.kuit_kac.global.util.TimeRange;

@RestController
@RequestMapping("/diets")
@Tag(name = "식단 관리", description = "식단 정보 조회, 생성, 수정, 삭제 등 식단 관련 기능을 제공합니다.")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final DietFoodService dietFoodService;

    @GetMapping("/records/profiles")
    @Operation(summary = "사용자 ID로 식단 기록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 오늘의 식단 기록을 조회합니다.")
    public ResponseEntity<List<DietRecordProfileResponse>> getDietRecords(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        TimeRange timeRange = getTodayDietTimeRange();
        List<Diet> diets = dietService.getDietsByUserId(userId, DietEntryType.RECORD);

        List<DietRecordProfileResponse> responses = diets.stream()
                .map(diet -> toDietRecordProfileResponse(diet, timeRange))
                .toList();

        return ResponseEntity.ok(responses);
    }

    private TimeRange getTodayDietTimeRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withHour(3).withMinute(0).withSecond(0).withNano(0);
        if (now.getHour() < 3) {
            start = start.minusDays(1);
        }
        LocalDateTime end = start.plusDays(1).withHour(2).withMinute(59).withSecond(59).withNano(999_999_999);
        return new TimeRange(start, end);
    }

    private DietRecordProfileResponse toDietRecordProfileResponse(Diet diet, TimeRange timeRange) {
        List<DietFood> dietFoods = dietFoodService.getDietFoodsByDietIdAndTimeRange(
                diet.getId(), timeRange.start(), timeRange.end());
        List<FoodProfileResponse> foodProfiles = dietFoods.stream()
                .map(dietFood -> FoodProfileResponse.from(dietFood, dietFood.getFood()))
                .toList();
        double totalKcal = foodProfiles.stream()
                .mapToDouble(food -> food.getQuantity() * food.getCalorie())
                .sum();

        return new DietRecordProfileResponse(
                diet.getId(),
                diet.getName(),
                diet.getDietType().getKoreanName(),
                diet.getDietEntryType().getKoreanName(),
                diet.getCreatedAt(),
                diet.getUpdatedAt(),
                totalKcal,
                foodProfiles
        );
    }
}