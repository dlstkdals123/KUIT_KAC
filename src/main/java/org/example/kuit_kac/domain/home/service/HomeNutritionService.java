package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.home.dto.HomeNutritionResponse;
import org.example.kuit_kac.global.util.TimeRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeNutritionService {
    // TODO: 오늘먹은 음식에서 탄수화물, 단백질, 당류, 지방의 목표량, 섭취량, 비율(%) 계산해서 제공)
    // TODO: 유저 id와 날짜로 음식가져오기
    // TODO: homeSummaryService에서 오늘목표섭취칼로리 가져오기
    DietFoodService dietFoodService; //
    HomeSummaryService homeSummaryService;

    @Transactional(readOnly = true)
    public HomeNutritionResponse getTodayNutrition(Long userId) {
        // 오늘 목표 칼로리 계산
        double todayTotalKCalorieGoal = homeSummaryService.calculateDailyKCalorieGoal(userId);

        TimeRange timeRange = TimeRange.getTodayTimeRange();
        LocalDateTime startOfDay = timeRange.start();
        LocalDateTime endOfDay = timeRange.end();

        // 날짜정보로 식단기록 가져오기
        List<DietFood> diets = dietFoodService.getDietFoodsByDietIdAndTimeRange(userId, startOfDay, endOfDay);

        // 목표량 계산
        int carbGoal = (int) (todayTotalKCalorieGoal * 0.45);
        int proteinGoal = (int) (todayTotalKCalorieGoal * 0.3);
        int sugarGoal = (int) (todayTotalKCalorieGoal * 0.08);
        int fatGoal = (int) (todayTotalKCalorieGoal * 0.25);

        // 섭취량 계산
        double carbTaken = 0;
        double proteinTaken = 0;
        double sugarTaken = 0;
        double fatTaken = 0;

        // 오늘 섭취 칼로리 계산
        for (DietFood dietFood : diets) {
            Food food = dietFood.getFood();
            double quantity = dietFood.getQuantity(); // 양 가져오기

            carbTaken += food.getCarbohydrate() * quantity;
            proteinTaken += food.getProtein() * quantity;
            sugarTaken += food.getSugar() * quantity;
            fatTaken += food.getFat() * quantity;
        }

        // 목표량 대비 섭취량 백분율 계산
        int carbRatio = (int) (carbTaken / carbGoal * 100);
        int proteinRatio = (int) (proteinTaken / proteinGoal * 100);
        int sugarRatio = (int) (sugarTaken / sugarGoal * 100);
        int fatRatio = (int) (fatTaken / fatGoal * 100);

        return new HomeNutritionResponse(
                carbGoal,
                proteinGoal,
                sugarGoal,
                fatGoal,
                (int) carbTaken,
                (int) proteinTaken,
                (int) sugarTaken,
                (int) fatTaken,
                carbRatio,
                proteinRatio,
                sugarRatio,
                fatRatio
        );
    }
}
