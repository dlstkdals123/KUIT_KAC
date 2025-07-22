package org.example.kuit_kac.domain.diet_food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodSnackCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodUpdateRequest;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.domain.diet.model.Diet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietFoodService {

    private final DietFoodRepository dietFoodRepository;
    private final FoodService foodService;

    public List<DietFood> createDietFoodsWithDietTime(List<DietFoodCreateRequest> foodRequests, Diet diet, LocalDateTime dietTime) {
        List<DietFood> dietFoods = foodRequests.stream()
                .map(foodReq -> {
                    Food food = foodService.getFoodById(foodReq.foodId());
                    return new DietFood(diet, food, foodReq.quantity(), dietTime);
                })
                .collect(Collectors.toList());
        return dietFoodRepository.saveAll(dietFoods);
    }

    public List<DietFood> createDietFoodsSnack(List<DietFoodSnackCreateRequest> foodRequests, Diet diet) {
        List<DietFood> dietFoods = foodRequests.stream()
                .map(foodReq -> {
                    Food food = foodService.getFoodById(foodReq.foodId());
                    return new DietFood(diet, food, foodReq.quantity(), foodReq.dietTime());
                })
                .collect(Collectors.toList());
        return dietFoodRepository.saveAll(dietFoods);
    }

    public DietFood updateDietFood(Long dietFoodId, DietFoodUpdateRequest request) {
        DietFood dietFood = dietFoodRepository.findById(dietFoodId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIET_FOOD_NOT_FOUND));
        dietFood.update(request.quantity(), request.dietTime());
        return dietFoodRepository.save(dietFood);
    }
}
