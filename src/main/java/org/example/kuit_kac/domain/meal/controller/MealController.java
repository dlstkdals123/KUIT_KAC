package org.example.kuit_kac.domain.meal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @PathVariable("id") Long id,
            @Valid @RequestBody MealUpdateRequest request
    ) {
        Meal meal = mealService.updateMealAndFoods(id, request);
        return ResponseEntity.ok(MealWithMealFoodsResponse.from(meal));
    }

    @GetMapping("/names")
    @Operation(summary = "사용자 ID와 끼니 유형으로 끼니 목록 이름 조회", description = "제공된 사용자 ID와 끼니 유형을 사용하여 해당 사용자의 모든 끼니 이름을 조회합니다.")
    public ResponseEntity<List<MealNameResponse>> getMealNames(
        @Valid @ModelAttribute MealSearchRequest mealSearchRequest) {
        Long userId = mealSearchRequest.getUserId();
        MealType mealType = mealSearchRequest.getMealType();

        List<Meal> meals = mealService.getMealsByUserIdAndMealType(userId, mealType);

        List<MealNameResponse> mealNameResponses = meals.stream()
                .map(MealNameResponse::from)
                .toList();

        return ResponseEntity.ok(mealNameResponses);
    }
}