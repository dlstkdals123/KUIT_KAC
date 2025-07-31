package org.example.kuit_kac.domain.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.dto.*;
import org.example.kuit_kac.domain.diet.model.*;
import org.example.kuit_kac.domain.diet.service.*;
import org.example.kuit_kac.domain.home.dto.*;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.home.service.CoachReportService;
import org.example.kuit_kac.domain.home.service.HomeNutritionService;
import org.example.kuit_kac.domain.home.service.HomeSummaryService;
import org.example.kuit_kac.domain.home.service.WeightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/home")
@Tag(name = "홈", description = "하루의 남은 칼로리, 일주일 요약 관련 기능을 제공합니다.")
@RequiredArgsConstructor
public class HomeController {

    private final HomeSummaryService homeSummaryService;
    private final WeightService weightService;
    private final HomeNutritionService homeNutritionService;
    private final CoachReportService coachReportService;

    @GetMapping("/summary")
    @Operation(summary = "홈 요약", description = "제공된 사용자 ID를 사용하여 오늘 남은 칼로리, 목표 일일 칼로리, 현재 체중을 제공합니다.")
    public ResponseEntity<HomeSummaryResponse> getHomeSummary(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        HomeSummaryResponse response = homeSummaryService.getTodayHomeSummary(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/weight")
    @Operation(summary = "")
    public ResponseEntity<Void> createWeight(@RequestBody HomeWeightRequest request) {
        weightService.saveOrUpdateTodayWeight(request.getUserId(), request.getWeight());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/weight/{userId}")
    public ResponseEntity<HomeWeightResponse> getLatestWeight(@PathVariable Long userId) {
        Weight weight = weightService.getLatestWeightByUserId(userId);
        return ResponseEntity.ok(HomeWeightResponse.from(weight));
    }

    @PutMapping("/weight")
    public ResponseEntity<Void> updateWeight(@RequestBody HomeWeightRequest request) {
        weightService.saveOrUpdateTodayWeight(request.getUserId(), request.getWeight());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nutrition")
    public ResponseEntity<HomeNutritionResponse> getNutrition(@PathVariable Long userId) {
        HomeNutritionResponse homeNutritionResponse = homeNutritionService.getTodayNutrition(userId);
        return ResponseEntity.ok(homeNutritionResponse);
    }

    @GetMapping("/coach-report")
    public ResponseEntity<HomeCoachReportResponse> getCoachReport(@PathVariable Long userId) {
        HomeCoachReportResponse homeCoachReportResponse = coachReportService.getCoachReport(userId);
        return ResponseEntity.ok(homeCoachReportResponse);
    }
}
