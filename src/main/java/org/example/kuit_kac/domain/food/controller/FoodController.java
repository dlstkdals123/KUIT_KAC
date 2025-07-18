package org.example.kuit_kac.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.example.kuit_kac.domain.food.dto.FoodNameResponse;
import org.example.kuit_kac.domain.food.dto.FoodResponse;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foods")
@Tag(name = "음식 관리", description = "음식 정보 조회, 생성, 수정, 삭제 등 음식 관련 모든 기능을 제공합니다.")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/{id}")
    @Operation(summary = "음식 ID로 단일 음식 정보 조회", description = "제공된 음식 ID를 사용하여 특정 음식의 상세 정보를 조회합니다.")
    public ResponseEntity<FoodResponse> getFood(
            @Parameter(description = "조회할 음식의 고유 ID", example = "1")
            @PathVariable("id") Long id
    ) {
        Food food = foodService.getFoodById(id);
        return ResponseEntity.ok(FoodResponse.from(food));
    }

    @GetMapping("/names/{name}")
    @Operation(summary = "음식 이름으로 단일 음식 정보 조회", description = "제공된 음식 이름을 사용하여 특정 음식의 상세 정보를 조회합니다.")
    public ResponseEntity<FoodResponse> getFoodByName(
            @Parameter(description = "조회할 음식의 이름", example = "치킨")
            @PathVariable("name") String name
    ) {
        Food food = foodService.getFoodByName(name);
        return ResponseEntity.ok(FoodResponse.from(food));
    }

    @GetMapping("/names")
    @Operation(summary = "모든 음식의 이름을 조회", description = "모든 음식의 ID와 이름을 조회합니다. local database 사용 시 삭제 예정입니다.")
    public ResponseEntity<List<FoodNameResponse>> getFoodNames() {
        List<Food> foods = foodService.findAll();
        List<FoodNameResponse> foodNameResponses = foods.stream()
                .map(FoodNameResponse::from)
                .toList();
        return ResponseEntity.ok(foodNameResponses); 
    }
}
