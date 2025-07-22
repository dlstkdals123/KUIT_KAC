package org.example.kuit_kac.domain.diet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.dto.*;
import org.example.kuit_kac.domain.diet.model.*;
import org.example.kuit_kac.domain.diet.service.*;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.example.kuit_kac.global.util.TimeRange;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/diets")
@Tag(name = "식단 관리", description = "식단을 생성하고 관리하는 API입니다.")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final UserService userService;
    private final DietFoodService dietFoodService;

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

    @GetMapping("/plans/profiles")
    @Operation(summary = "사용자 ID로 오늘의 계획(Plan) 식단 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 오늘의 계획(Plan) 식단을 조회합니다.")
    public ResponseEntity<List<DietRecordProfileResponse>> getDietPlans(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        TimeRange timeRange = TimeRange.getTodayDietTimeRange();
        List<Diet> diets = dietService.getDietsByUserId(userId, DietEntryType.PLAN);

        List<DietRecordProfileResponse> responses = diets.stream()
                .map(diet -> DietRecordProfileResponse.from(diet, timeRange))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/template")
    @Operation(summary = "나만의 식단 생성", description = "유저 ID와 식단 이름, 음식을 입력하여 나만의 식단을 생성합니다.")
    public ResponseEntity<DietRecordProfileResponse> createTemplateDiet(
            @RequestBody @Valid DietTemplateCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Diet diet = dietService.createTemplateDiet(user, request.name(), request.dietTime(), request.foods());
        DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simple")
    @Operation(summary = "단식, 외식, 술자리 식단 생성", description = "유저 ID와 식단 항목 종류를 입력하여 음식을 포함하지 않는 식단을 생성합니다.")
    public ResponseEntity<DietRecordProfileResponse> createSimpleDiet(
            @RequestBody @Valid DietSimpleCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Diet diet = dietService.createSimpleDiet(user, request.dietType(), request.dietEntryType());
        DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/general")
    @Operation(summary = "식단, 계획, AI 계획 식단 생성", description = "유저 ID와 식단 이름, 식단 항목 종류, 식단 음식을 입력하여 음식을 포함하는 식단을 생성합니다.")
    public ResponseEntity<DietRecordProfileResponse> createGeneralDiet(
            @RequestBody @Valid DietGeneralCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Diet diet = dietService.createGeneralDiet(user, request.name(), request.dietType(), request.dietEntryType(), request.dietTime(), request.foods());
        DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/snack")
    @Operation(summary = "간식 식단 생성", description = "유저 ID와 식단 음식 섭취 시간을 입력하여 간식 식단을 생성합니다.")
    public ResponseEntity<DietRecordProfileResponse> createSnackDiet(
            @RequestBody @Valid DietSnackCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Diet diet = dietService.createSnackDiet(user, request.name(), request.dietEntryType(), request.foods());
        DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{dietId}/foods")
    @Operation(summary = "식단에 음식 추가", description = "식단 ID와 음식을 입력하여 식단에 음식을 추가합니다.")
    public ResponseEntity<DietRecordProfileResponse> addFoodToDiet(
            @PathVariable("dietId") Long dietId, 
            @RequestBody @Valid List<DietFoodCreateRequest> requests,
            @RequestParam("dietTime") LocalDateTime dietTime
    ) {
        Diet diet = dietService.getDietById(dietId);
        List<DietFood> newDietFoods = dietFoodService.createDietFoodsWithDietTime(requests, diet, dietTime);
        
        newDietFoods.forEach(diet::addDietFood);
        dietFoodService.updateDietFoodsWithDietTime(diet.getDietFoods(), dietTime);
        
        DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
        return ResponseEntity.ok(response);
    }
}