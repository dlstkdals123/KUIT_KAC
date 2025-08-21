package org.example.kuit_kac.domain.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.home.dto.*;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.home.service.CoachReportService;
import org.example.kuit_kac.domain.home.service.HomeNutritionService;
import org.example.kuit_kac.domain.home.service.HomeSummaryService;
import org.example.kuit_kac.domain.home.service.WeightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@Tag(name = "홈", description = "하루의 남은 칼로리, 일주일 요약 관련 기능을 제공합니다.")
@RequiredArgsConstructor
public class HomeController {

    private final HomeSummaryService homeSummaryService;
    private final WeightService weightService;
    private final HomeNutritionService homeNutritionService;
    private final CoachReportService coachReportService;

    @GetMapping
    public String home() {
        return "로그인 성공";
    }

    @GetMapping("/summary")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "홈 요약", description = "제공된 사용자 ID를 사용하여 오늘 남은 칼로리, 목표 일일 칼로리, 현재 체중을 제공합니다.")
    public ResponseEntity<HomeSummaryResponse> getHomeSummary(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        HomeSummaryResponse response = homeSummaryService.getTodayHomeSummary(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/weight")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "체중 등록", description = "오늘 날짜에 처음 체중을 기록합니다.")
    public ResponseEntity<Void> createWeight(
            @RequestBody @Valid HomeWeightRequest request) {
        weightService.saveOrUpdateTodayWeight(request.getUserId(), request.getWeight());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/weight/{userId}")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "체중 불러오기", description = "제공된 사용자 ID를 사용하여 가장 최근에 기록된 체중을 제공합니다.")
    public ResponseEntity<HomeWeightResponse> getLatestWeight(
            @PathVariable Long userId) {
        Weight weight = weightService.getLatestWeightByUserId(userId);
        return ResponseEntity.ok(HomeWeightResponse.from(weight));
    }

    @PutMapping("/weight")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "체중 수정", description = "오늘 이미 기록된 체중이 있을 경우, 새로운 값으로 수정합니다.")
    public ResponseEntity<Void> updateWeight(
            @RequestBody @Valid HomeWeightRequest request) {
        weightService.saveOrUpdateTodayWeight(request.getUserId(), request.getWeight());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nutrition")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "그날의 섭취 영양 정보", description = "오늘 먹은 영양소 중 탄수화물/단백질/당류/지방 각각의 영양소에서 목표량, 섭취량, 목표량 대비 섭취량의 비율을 제공합니다.")
    public ResponseEntity<HomeNutritionResponse> getNutrition(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {
        HomeNutritionResponse homeNutritionResponse = homeNutritionService.getTodayNutrition(userId);
        return ResponseEntity.ok(homeNutritionResponse);
    }

    @GetMapping("/coach-report")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "햄코치의 관찰일지", description = "외식 횟수, 공복시간 정도, 술자리/배달어플/야식 빈도를 제공합니다.")
    public ResponseEntity<HomeCoachReportResponse> getCoachReport(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {
        HomeCoachReportResponse homeCoachReportResponse = coachReportService.getCoachReport(userId);
        return ResponseEntity.ok(homeCoachReportResponse);
    }
}
