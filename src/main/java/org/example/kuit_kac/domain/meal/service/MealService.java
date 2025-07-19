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

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    private final MealFoodService mealFoodService;

    @Transactional(readOnly = true)
    public List<Meal> getMeals() {
        List<Meal> meals = mealRepository.findAll();
        if (meals.isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }
        return meals;
    }

    @Transactional(readOnly = true)
    public List<Meal> getMealsByUserId(Long userId) {
        List<Meal> meals = mealRepository.findByUserId(userId);
        if (meals.isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }
        return meals;
    }

    @Transactional(readOnly = true)
    public List<Meal> getMealsByMealType(MealType mealType) {
        List<Meal> meals = mealRepository.findByMealType(mealType);
        if (meals.isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }
        return meals;
    }

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

        Optional.ofNullable(request.getMealType()).ifPresent(meal::setMealType);
        Optional.ofNullable(request.getMealTime()).ifPresent(meal::setMealTime);

        meal.getMealFoods().clear();

        request.getFoods().forEach(foodRequest -> {
            Food food = foodRepository.findById(foodRequest.getFoodId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

            MealFood newMealFood = new MealFood(meal, food, foodRequest.getQuantity());
            meal.addMealFood(newMealFood);
        });

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