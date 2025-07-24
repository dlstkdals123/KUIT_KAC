package org.example.kuit_kac.domain.diet_food.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/diet-foods")
@Tag(name = "식단 음식 관리", description = "식단 음식을 관리하는 API입니다.")
@RequiredArgsConstructor
public class DietFoodController {
} 