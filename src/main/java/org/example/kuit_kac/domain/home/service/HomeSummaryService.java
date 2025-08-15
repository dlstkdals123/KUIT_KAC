package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.home.dto.HomeSummaryResponse;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
<<<<<<< Updated upstream
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
=======
import org.example.kuit_kac.domain.user_information.service.UserInfoService;
>>>>>>> Stashed changes
import org.example.kuit_kac.global.util.TimeRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
// TODO: 컨트롤러로 로직분리
public class HomeSummaryService {
    private final DietRepository dietRepository;
    private final DietFoodRepository dietFoodRepository;
    private final UserService userService;
    private final OnboardingService onboardingService;
    private final WeightService weightService;

    // 하루 섭취 영양소 요약
    @Transactional(readOnly = true)
    public HomeSummaryResponse getTodayHomeSummary(Long userId) {
        // 가져와야하는 정보: 일일섭취목표 - 오늘 먹은 음식들의 칼로리 합산 - 운동해서 소모한 칼로리 = 몇칼로리 남았는지 계산(음수 반환 가능)

        TimeRange timeRange = TimeRange.getTodayTimeRange();
        LocalDateTime startOfDay = timeRange.start();
        LocalDateTime endOfDay = timeRange.end();

        // 날짜정보로 식단기록 가져오기
        List<DietFood> dietFoods = dietFoodRepository.findByDietUserIdAndDietTimeBetween(userId, startOfDay, endOfDay);

        // 오늘 섭취 칼로리 계산
        double totalKCalorie = 0;
        for (DietFood dietFood : dietFoods) {
            Food food = dietFood.getFood();
            double quantity = dietFood.getQuantity(); // 양 가져오기
            double unitCalorie = food.getCalorie(); // 단위당 칼로리 가져오기
            totalKCalorie += quantity * unitCalorie;
        }

        double currentWeight = weightService.getLatestWeightByUserId(userId).getWeight();
        // TODO 운동소모 칼로리 더미데이터 300.0kcal
        double remainingKCalorie = totalKCalorie - 300.0;

        return new HomeSummaryResponse(
                calculateDailyKCalorieGoal(userId), // 일일섭취목표
                totalKCalorie,
                currentWeight,
                remainingKCalorie
        );
    }

    // 일일섭취목표 칼로리 계산
    public double calculateDailyKCalorieGoal(Long userId) {
        // 일일섭취목표 : BMR - 일일감량목표칼로리

        User user = userService.getUserById(userId);
        UserInformation userInfo = onboardingService.getUserInformationByUserId(userId);
        double weightValue = weightService.getLatestWeightByUserId(userId).getWeight();

        int age = user.getAge();
        //기초대사량 계산
        double bmr = (user.getGender() == GenderType.MALE)
                ? 10 * user.getTargetWeight() + 6.25 * user.getHeight() - 5 * age + 5
                : 10 * user.getTargetWeight() + 6.25 * user.getHeight() - 5 * age - 161;
        // 목표까지 감량해야 할 몸무게
        double TargetWeightLoss = weightValue - user.getTargetWeight();
        int dietDays = userInfo.getDietVelocity().getPeriodInDays(); // 다이어트기간 '일'단위로 계산

        double dailyDeficit = (TargetWeightLoss * 7700) / dietDays; // 감량칼로리 / 다이어트기간

        return bmr - dailyDeficit;
    }
}
