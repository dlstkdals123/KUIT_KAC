package org.example.kuit_kac.domain.home.service;

import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.home.dto.HomeNutritionResponse;

public class HomeNutritionService {
    // TODO: 오늘먹은 음식에서 탄수화물, 단백질, 당류, 지방의 목표량, 섭취량, 비율(%) 계산해서 제공)
    // TODO: 유저 id와 날짜로 음식가져오기
    // TODO: homeSummaryService에서 오늘목표섭취칼로리 가져오기
    DietService dietService;
    HomeSummaryService homeSummaryService;
    public HomeNutritionResponse getTodayNutrition(Long userId) {
        double todayTotalKCalorieGoal = homeSummaryService.calculateDailyKCalorieGoal(userId);


    }
}
