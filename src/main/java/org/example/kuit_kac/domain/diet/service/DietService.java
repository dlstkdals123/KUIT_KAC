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
import org.example.kuit_kac.domain.home.model.FoodSummary;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.util.EnumConverter;
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

        // 한국어를 ENUM으로 변환하여 설정
        Optional.ofNullable(request.getDietType())
                .map(EnumConverter::fromKoreanDietType)
                .ifPresent(diet::setDietType);

        Optional.ofNullable(request.getDietEntryType())
                .map(EnumConverter::fromKoreanDietEntryType)
                .ifPresent(diet::setDietEntryType);

        Optional.ofNullable(request.getDietTime()).ifPresent(diet::setDietTime);

        return diet;
    }

    @Transactional
    public Diet createDietWithDietFoods(DietCreateRequest dietCreateRequest, User user) {
        if (dietCreateRequest.getDietFoods() == null || dietCreateRequest.getDietFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }

        Diet diet = new Diet(user, dietCreateRequest.getName(), dietCreateRequest.getDietTypeEnum(), dietCreateRequest.getDietEntryTypeEnum(), dietCreateRequest.getDietTime());

        dietRepository.save(diet);

        dietCreateRequest.getDietFoods().forEach(dietFoodCreateRequest -> {
            DietFood dietFood = dietFoodService.createDietFoodWithFoods(dietFoodCreateRequest, diet);
            diet.addDietFood(dietFood);
        });
        return diet;
    }

    @Transactional(readOnly = true)
    public List<FoodSummary> getTodayFoodSummary(long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1); // 23:59:59

        List<Diet> diets = dietRepository.findByUserIdAndDietTimeBetween(userId, startOfDay, endOfDay);

        List<FoodSummary> foodSummaries = new ArrayList<>();

        for (Diet diet : diets) {
            for (DietFood dietFood : diet.getDietFoods()) {
                Food food = dietFood.getFood();
                double quantity = dietFood.getQuantity();

                double unitCalorie = food.getCalorie();
                double unitCarb = food.getCarbohydrate();
                double unitProtein = food.getProtein();
                double unitFat = food.getFat();

                double totalCalorie = unitCalorie * quantity;
                double totalCarb = unitCarb * quantity;
                double totalProtein = unitProtein * quantity;
                double totalFat = unitFat * quantity;

                double calorieSum = totalCarb * 4 + totalProtein * 4 + totalFat * 9;
                if (calorieSum == 0) calorieSum = 1; // 0으로 나눔 방지

                double carbRatio = (totalCarb * 4) / calorieSum;
                double proteinRatio = (totalCarb * 4) / calorieSum;
                double fatRatio = (totalFat * 4) / calorieSum;

                foodSummaries.add(
                        new FoodSummary(
                                food.getId(),
                                quantity,
                                round(carbRatio),
                                round(proteinRatio),
                                round(fatRatio),
                                round(totalCalorie)
                        )
                );
            }
        }
        return foodSummaries;
    }

    // 소수점 둘째자리까지 반올림
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}