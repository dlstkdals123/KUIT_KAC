package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.dto.DietCreateRequest;
import org.example.kuit_kac.domain.diet.dto.DietUpdateRequest;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodUpdateRequest;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DietService {

    private final DietRepository dietRepository;
    private final FoodRepository foodRepository;

    private final DietFoodService dietFoodService;

    @Transactional(readOnly = true)
    public List<Diet> getDietsByUserId(Long userId, DietEntryType dietEntryType, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59
        List<Diet> diets = dietRepository.findByUserIdAndDietEntryTypeAndDietTimeBetween(userId, dietEntryType, startOfDay, endOfDay);
        return diets;
    }

    @Transactional(readOnly = true)
    public List<Diet> getDietsByUserIdAndDietType(Long userId, DietType dietType) {
        List<Diet> diets = dietRepository.findByUserIdAndDietType(userId, dietType);
        if (diets.isEmpty()) {
            throw new CustomException(ErrorCode.DIET_NOT_FOUND);
        }
        return diets;
    }

    @Transactional
    public Diet updateDietAndFoods(Long dietId, DietUpdateRequest request) {
        if (request.getFoods() == null || request.getFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }
        
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEAL_NOT_FOUND));

        // 1. 먼저 모든 Food가 존재하는지 검증
        List<Food> foods = new ArrayList<>();
        for (DietFoodUpdateRequest foodRequest : request.getFoods()) {
            Food food = foodRepository.findById(foodRequest.getFoodId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
            foods.add(food);
        }

        // 2. 검증 완료 후 기존 데이터 삭제
        diet.getDietFoods().clear();

        // 3. 새로운 데이터 추가
        for (int i = 0; i < request.getFoods().size(); i++) {
            DietFoodUpdateRequest foodRequest = request.getFoods().get(i);
            Food food = foods.get(i);
            
            DietFood newDietFood = new DietFood(diet, food, foodRequest.getQuantity());
            diet.addDietFood(newDietFood);
        }

        Optional.ofNullable(request.getDietType()).ifPresent(diet::setDietType);
        Optional.ofNullable(request.getDietTime()).ifPresent(diet::setDietTime);

        return diet;
    }

    @Transactional
    public Diet createDietWithDietFoods(DietCreateRequest dietCreateRequest, User user) {
        if (dietCreateRequest.getDietFoods() == null || dietCreateRequest.getDietFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }

        Diet diet = new Diet(user, dietCreateRequest.getName(), dietCreateRequest.getDietType(), dietCreateRequest.getDietEntryType(), dietCreateRequest.getDietTime());

        dietRepository.save(diet);

        dietCreateRequest.getDietFoods().forEach(dietFoodCreateRequest -> {
            DietFood dietFood = dietFoodService.createDietFoodWithFoods(dietFoodCreateRequest, diet);
            diet.addDietFood(dietFood);
        });
        return diet;
    }
}