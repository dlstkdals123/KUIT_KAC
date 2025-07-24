package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.home.dto.HomeSummaryResponse;
import org.example.kuit_kac.domain.home.model.FoodSummary;
import org.example.kuit_kac.domain.home.repository.WeightRepository;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeSummaryService {
    private final DietService dietService;
    private final DietRepository dietRepository;

    // 하루 섭취 영양소 요약
    @Transactional(readOnly = true)
    public HomeSummaryResponse getTodayHomeSummary(long userId, LocalDate date) {

        // 합친 코드 전
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



        // 합친 코드 후

        double totalCalorie = 0.0;
        double carbohydrate = 0.0;
        double protein = 0.0;
        double fat = 0.0;

        for (FoodSummary summary : foodSummaries) {
            totalCalorie += summary.getTotalCalorie();
            carbohydrate += summary.getCarbohydrateRatio() * summary.getTotalCalorie();
            protein += summary.getProteinRatio() * summary.getTotalCalorie();
            fat += summary.getFatRatio() * summary.getTotalCalorie();
        }

        double carbRatio = (carbohydrate / 4) / (totalCalorie == 0 ? 1 : totalCalorie);
        double proteinRatio = (protein / 4) / (totalCalorie == 0 ? 1 : totalCalorie);
        double fatRatio = (fat / 4) / (totalCalorie == 0 ? 1 : totalCalorie);

        double dailyTarget = 11111;
        double remaining = dailyTarget - totalCalorie;

        return new HomeSummaryResponse(
                foodSummaries,
                round(totalCalorie),
                round(remaining),
                round(carbRatio),
                round(proteinRatio),
                round(fatRatio)
        );

    }

    // 소수점 둘째자리까지 반올림
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // 기초대사량 계산
    public double calculateDailyCalorieGoal(User user, UserInformation info, double currentWeight) {
        int age = user.getAge();
        double bmr = (user.getGender() == GenderType.MALE)
                ? 10 * user.getTargetWeight() + 6.25 * user.getHeight() - 5 * age + 5
                : 10 * user.getTargetWeight() + 6.25 * user.getHeight() - 5 * age - 161;
        double weightLoss = currentWeight - user.getTargetWeight();
        int dietDays = info.getDietVelocity().getPeriodInDays();

        double dailyDeficit = (weightLoss * 7700) / dietDays; // 일일감량칼로리 / 다이어트기간

        return bmr - dailyDeficit;
    }
}
