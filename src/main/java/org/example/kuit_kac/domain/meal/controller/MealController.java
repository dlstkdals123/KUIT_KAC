package org.example.kuit_kac.domain.meal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.meal.dto.*;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal.model.MealType;
import org.example.kuit_kac.domain.meal.service.MealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meals")
@Tag(name = "끼니 관리", description = "끼니 정보 조회, 생성, 수정, 삭제 등 끼니 관련 기능을 제공합니다.")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PutMapping("/{id}")
    @Operation(summary = "특정 끼니 전체 업데이트", description = "지정된 끼니(Meal)의 모든 정보를 새로운 내용으로 교체하고, 포함된 모든 음식(MealFood) 정보도 새로운 내용으로 덮어씁니다.")
    public ResponseEntity<MealWithMealFoodsResponse> updateMealWithFoods(
            @Parameter(description = "수정할 끼니의 고유 ID", example = "10")
            @PathVariable Long id,
            @RequestBody MealUpdateRequest request
    ) {
        Meal meal = mealService.updateMealAndFoods(id, request);
        return ResponseEntity.ok(MealWithMealFoodsResponse.from(meal));
    }

    @GetMapping()
    @Operation(summary = "사용자 ID로 끼니 이름 목록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 모든 끼니를 조회합니다.")
    public ResponseEntity<List<MealResponse>> getMeals(@ModelAttribute MealSearchRequest mealSearchRequest) {
        List<Meal> meals = getMealsByMealSearchRequest(mealSearchRequest);

        List<MealResponse> mealResponses = meals.stream()
                .map(MealResponse::from)
                .toList();

        return ResponseEntity.ok(mealResponses);
    }

    @GetMapping("/names")
    @Operation(summary = "사용자 ID로 끼니 이름 목록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 모든 끼니를 조회합니다.")
    public ResponseEntity<List<MealNameResponse>> getMealNames(@ModelAttribute MealSearchRequest mealSearchRequest) {
        List<Meal> meals = getMealsByMealSearchRequest(mealSearchRequest);

        List<MealNameResponse> mealNameResponses = meals.stream()
                .map(MealNameResponse::from)
                .toList();

        return ResponseEntity.ok(mealNameResponses);
    }

    private List<Meal> getMealsByMealSearchRequest(MealSearchRequest mealSearchRequest) {
        Long userId = mealSearchRequest.getUserId();
        MealType mealType = mealSearchRequest.getMealType();
        List<Meal> meals;

        if (userId == null && mealType == null) {
            meals = mealService.getMeals();
        } else if (userId == null) {
            meals = mealService.getMealsByMealType(mealType);
        } else if (mealType == null) {
            meals = mealService.getMealsByUserId(userId);
        } else {
            meals = mealService.getMealsByUserIdAndMealType(userId, mealType);
        }

        return meals;
    }
}