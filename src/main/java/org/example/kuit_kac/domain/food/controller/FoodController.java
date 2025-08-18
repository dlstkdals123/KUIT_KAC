package org.example.kuit_kac.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;
import org.example.kuit_kac.domain.food.model.Aifood;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/foods")
@Tag(name = "음식 관리", description = "음식을 생성하고 관리하는 API입니다.")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/aifoods")
    @Operation(summary = "사용자 ID로 AI 음식 목록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 모든 AI 음식을 조회합니다.")
    public ResponseEntity<List<FoodProfileResponse>> getAifoodsByUserId(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        List<Aifood> aifoods = foodService.getAifoodsByUserId(userId);

        List<FoodProfileResponse> responses = aifoods.stream()
                .map(FoodProfileResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
