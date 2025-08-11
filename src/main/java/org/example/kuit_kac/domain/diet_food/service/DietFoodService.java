package org.example.kuit_kac.domain.diet_food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodSnackCreateRequest;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                    LocalDateTime dietDateTime = foodReq.dietTime().atDate(LocalDate.now());
                    return new DietFood(diet, food, foodReq.quantity(), dietDateTime);
                })
                .collect(Collectors.toList());
        return dietFoodRepository.saveAll(dietFoods);
    }

    public void deleteDietFoods(List<DietFood> dietFoods) {
        dietFoodRepository.deleteAll(dietFoods);
    }

    @Transactional(readOnly = true)
    public List<DietFood> getDietFoodsByDietIdAndTimeRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return dietFoodRepository.findByDietUserIdAndDietTimeBetween(userId, start, end);
    }

}
