package org.example.kuit_kac.domain.food.controller;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.food.dto.FoodResponse;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @GetMapping()
    public List<FoodResponse> getAll() { return foodService.getAllFoods(); }
}
