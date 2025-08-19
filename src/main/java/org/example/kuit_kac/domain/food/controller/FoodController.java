package org.example.kuit_kac.domain.food.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foods")
@Tag(name = "음식 관리", description = "음식을 생성하고 관리하는 API입니다.")
@RequiredArgsConstructor
public class FoodController {
}
