package org.example.kuit_kac.domain.diet_food.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DietFoodService {

    private final DietFoodRepository dietFoodRepository;

    private final FoodService foodService;

    @Transactional
    public DietFood createDietFoodWithFoods(DietFoodCreateRequest dietFoodCreateRequest, Diet diet) {
        Food food = foodService.getFoodById(dietFoodCreateRequest.getFoodId());

        DietFood dietFood = new DietFood(diet, food, dietFoodCreateRequest.getQuantity());

        dietFood.setFood(food);

        return dietFoodRepository.save(dietFood);
    }
}
