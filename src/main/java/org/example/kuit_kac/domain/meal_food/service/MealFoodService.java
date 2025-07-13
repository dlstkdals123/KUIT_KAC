package org.example.kuit_kac.domain.meal_food.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal_food.dto.MealFoodCreateRequest;
import org.example.kuit_kac.domain.meal_food.model.MealFood;
import org.example.kuit_kac.domain.meal_food.repository.MealFoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MealFoodService {

    private final MealFoodRepository mealFoodRepository;

    private final FoodService foodService;

    @Transactional
    public MealFood createMealFoodWithFoods(MealFoodCreateRequest mealFoodCreateRequest, Meal meal) {
        Food food = foodService.getFoodById(mealFoodCreateRequest.getFoodId());

        MealFood mealFood = new MealFood(meal, food, mealFoodCreateRequest.getQuantity());
        mealFood.addFood(food);

        return mealFoodRepository.save(mealFood);
    }
}
