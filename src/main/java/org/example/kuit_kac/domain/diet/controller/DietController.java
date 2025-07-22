package org.example.kuit_kac.domain.diet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.dto.*;
import org.example.kuit_kac.domain.diet.model.*;
import org.example.kuit_kac.domain.diet.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.example.kuit_kac.global.util.TimeRange;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diets")
@Tag(name = "식단 관리", description = "식단 정보 조회, 생성, 수정, 삭제 등 식단 관련 기능을 제공합니다.")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final DietFoodService dietFoodService;
    private final UserService userService;

    @GetMapping("/records/profiles")
    @Operation(summary = "사용자 ID로 식단 기록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 오늘의 식단 기록을 조회합니다.")
    public ResponseEntity<List<DietRecordProfileResponse>> getDietRecords(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        TimeRange timeRange = TimeRange.getTodayDietTimeRange();
        List<Diet> diets = dietService.getDietsByUserId(userId, DietEntryType.RECORD);

        List<DietRecordProfileResponse> responses = diets.stream()
                .map(diet -> DietRecordProfileResponse.from(diet, timeRange))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "식단 생성", description = "식단과 식단에 포함된 음식들을 생성합니다.")
    @Transactional
    public ResponseEntity<Long> createDiet(@RequestBody DietCreateRequest request) {
        User user = userService.getUserById(request.userId());

        Diet diet = dietService.createDiet(user, DietType.getDietType(request.dietType()));

        dietFoodService.createDietFoods(request.foods(), diet);
        return ResponseEntity.ok(diet.getId());
    }
}