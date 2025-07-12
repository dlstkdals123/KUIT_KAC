package org.example.kuit_kac.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.food.dto.FoodResponse;
import org.example.kuit_kac.domain.food.service.FoodService;
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
    public FoodResponse getFood(@PathVariable Long id) { return foodService.getFoodById(id); }
}
