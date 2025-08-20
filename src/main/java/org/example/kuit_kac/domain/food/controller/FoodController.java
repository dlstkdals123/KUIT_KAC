package org.example.kuit_kac.domain.food.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foods")
@Tag(name = "음식 관리", description = "음식을 생성하고 관리하는 API입니다.")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @Value("${default-food-size}")
    private Long defaultFoodSize;

    @GetMapping("")
    public ResponseEntity<List<FoodProfileResponse>> getFoods() {
        List<Food> foods = foodService.getFoodsAfter(defaultFoodSize);
        List<FoodProfileResponse> foodProfileResponses = foods.stream()
            .map(FoodProfileResponse::from)
            .toList();
        return ResponseEntity.ok(foodProfileResponses);
    }
}
