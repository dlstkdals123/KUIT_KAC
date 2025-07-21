package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.home.dto.HomeSummaryResponse;
import org.example.kuit_kac.domain.home.model.FoodSummary;
import org.example.kuit_kac.domain.home.repository.WeightRepository;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeSummaryService {
    private final DietService dietService;

    @Transactional(readOnly = true)
    public HomeSummaryResponse getTodayHomeSummary(long userId) {
        List<FoodSummary> foodSummaries = dietService.getTodayFoodSummary(userId, LocalDate.now());

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
}
