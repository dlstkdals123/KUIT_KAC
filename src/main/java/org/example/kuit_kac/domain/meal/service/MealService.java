package org.example.kuit_kac.domain.meal.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.domain.meal.dto.MealUpdateRequest;
import org.example.kuit_kac.domain.meal.dto.MealWithFoodsResponse;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal_food.model.MealFood;
import org.example.kuit_kac.domain.meal.repository.MealRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public MealWithFoodsResponse updateMealAndFoods(Long mealId, MealUpdateRequest request) {
        if (request.getFoods() == null || request.getFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEAL_NOT_FOUND));

        Optional.ofNullable(request.getMealType()).ifPresent(meal::setMealType);
        Optional.ofNullable(request.getMealTime()).ifPresent(meal::setMealTime);

        meal.getMealFoods().clear();

        request.getFoods().forEach(foodRequest -> {
            Food food = foodRepository.findById(foodRequest.getFoodId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

            MealFood newMealFood = new MealFood(meal, food, foodRequest.getQuantity());
            meal.addMealFood(newMealFood);
        });

        return MealWithFoodsResponse.from(meal);
    }
}