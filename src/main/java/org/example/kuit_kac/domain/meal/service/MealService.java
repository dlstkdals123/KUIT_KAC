package org.example.kuit_kac.domain.meal.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.domain.meal.dto.MealCreateRequest;
import org.example.kuit_kac.domain.meal.dto.MealUpdateRequest;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal.model.MealType;
import org.example.kuit_kac.domain.meal.repository.MealRepository;
import org.example.kuit_kac.domain.meal_food.model.MealFood;
import org.example.kuit_kac.domain.meal_food.service.MealFoodService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.example.kuit_kac.domain.meal_food.dto.MealFoodUpdateRequest;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    private final MealFoodService mealFoodService;

    @Transactional(readOnly = true)
    public List<Meal> getMealsByUserIdAndMealType(Long userId, MealType mealType) {
        List<Meal> meals = mealRepository.findByUserIdAndMealType(userId, mealType);
        if (meals.isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }
        return meals;
    }

    @Transactional
    public Meal updateMealAndFoods(Long mealId, MealUpdateRequest request) {
        if (request.getFoods() == null || request.getFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }
        
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEAL_NOT_FOUND));

        // 1. 먼저 모든 Food가 존재하는지 검증
        List<Food> foods = new ArrayList<>();
        for (MealFoodUpdateRequest foodRequest : request.getFoods()) {
            Food food = foodRepository.findById(foodRequest.getFoodId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
            foods.add(food);
        }

        // 2. 검증 완료 후 기존 데이터 삭제
        meal.getMealFoods().clear();

        // 3. 새로운 데이터 추가
        for (int i = 0; i < request.getFoods().size(); i++) {
            MealFoodUpdateRequest foodRequest = request.getFoods().get(i);
            Food food = foods.get(i);
            
            MealFood newMealFood = new MealFood(meal, food, foodRequest.getQuantity());
            meal.addMealFood(newMealFood);
        }

        Optional.ofNullable(request.getMealType()).ifPresent(meal::setMealType);
        Optional.ofNullable(request.getMealTime()).ifPresent(meal::setMealTime);

        return meal;
    }

    @Transactional
    public Meal createMealWithMealFoods(MealCreateRequest mealCreateRequest, Diet diet, User user) {
        if (mealCreateRequest.getMealFoods() == null || mealCreateRequest.getMealFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }

        Meal meal = new Meal(user, diet, mealCreateRequest.getName(), mealCreateRequest.getMealType(), mealCreateRequest.getMealTime());

        mealRepository.save(meal);

        mealCreateRequest.getMealFoods().forEach(mealFoodCreateRequest -> {
            MealFood mealFood = mealFoodService.createMealFoodWithFoods(mealFoodCreateRequest, meal);
            meal.addMealFood(mealFood);
        });
        return meal;
    }
}