package org.example.kuit_kac.domain.diet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietCreateRequest;
import org.example.kuit_kac.domain.diet.dto.DietSearchRequest;
import org.example.kuit_kac.domain.diet.dto.DietWithMealsAndFoodsResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diets")
@Tag(name = "식단 관리", description = "사용자 식단 정보 조회, 생성, 수정, 삭제 등 식단 관련 모든 기능을 제공합니다.")
@RequiredArgsConstructor
public class DietController {
    private final DietService dietService;

    @GetMapping()
    @Operation(summary = "사용자 식단 기록 조회",
            description = "특정 사용자의 날짜별 식단 기록을 조회합니다. 식단 유형으로 필터링할 수 있습니다.")
    public ResponseEntity<DietWithMealsAndFoodsResponse> getDietByUserId(
        @Valid @ModelAttribute DietSearchRequest dietSearchCondition) {
        Diet diet = dietService.getDietByUserIdAndDietTypeAndDietDate(
                dietSearchCondition.getUserId(),
                dietSearchCondition.getDietType(),
                dietSearchCondition.getDietDate());

        return ResponseEntity.ok(DietWithMealsAndFoodsResponse.from(diet));
    }

    @PostMapping()
    @Operation(summary = "새로운 식단 기록 생성", description = "사용자의 새로운 식단 기록을 생성합니다. 식단 유형(일반 식사 또는 단식)과 포함된 끼니 및 음식 정보에 따라 다르게 처리됩니다.")
    public ResponseEntity<DietWithMealsAndFoodsResponse> createDiet(
        @Valid @RequestBody DietCreateRequest dietCreateRequest) {
        Diet diet = dietService.createDiet(dietCreateRequest);

        return ResponseEntity.ok(DietWithMealsAndFoodsResponse.from(diet));
    }

}