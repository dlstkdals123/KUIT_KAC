package org.example.kuit_kac.domain.diet_food.controller;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.dto.DietRecordProfileResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodUpdateRequest;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/diet-foods")
@Tag(name = "식단 음식 관리", description = "식단 음식을 관리하는 API입니다.")
@RequiredArgsConstructor
public class DietFoodController {

    private final DietFoodService dietFoodService;

    @PatchMapping("/general/{dietFoodId}")
    @Operation(summary = "식단 음식 수정", description = "식단 음식 ID를 입력하여 식단 음식을 수정합니다.")
    public ResponseEntity<DietRecordProfileResponse> updateDietFood(
        @PathVariable("dietFoodId") Long dietFoodId,
        @RequestBody @Valid DietFoodUpdateRequest request) {
        //TODO: 식단 유형 종류 확인
        DietFood dietFood = dietFoodService.updateDietFood(dietFoodId, request);

        Diet diet = dietFood.getDiet();
        dietFoodService.updateDietFoodsWithDietTime(diet.getDietFoods(), request.dietTime());
        
        DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
        return ResponseEntity.ok(response);
    }


} 