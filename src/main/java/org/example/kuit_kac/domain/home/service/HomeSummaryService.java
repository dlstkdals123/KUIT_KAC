package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.home.dto.HomeSummaryResponse;
import org.example.kuit_kac.domain.routine.model.RoutineDetail;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;
import org.example.kuit_kac.domain.routine.model.RoutineType;
import org.example.kuit_kac.domain.routine.repository.RoutineDetailRepository;
import org.example.kuit_kac.domain.routine.repository.RoutineExerciseRepository;
import org.example.kuit_kac.domain.routine.repository.RoutineRepository;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.example.kuit_kac.global.util.TimeRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeSummaryService {
    private final DietFoodRepository dietFoodRepository;
    private final UserService userService;
    //    private final OnboardingService onboardingService;
    private final UserInfoRepository userInfoRepository;
    private final WeightService weightService;
    private final RoutineRepository routineRepository;
    private final RoutineExerciseRepository reRepository;

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

        // 오늘 소모 운동칼로리 계산
        double exerciseKCalorie = 0;
        exerciseKCalorie = getExerciseKCalorie(userId, startOfDay, endOfDay, exerciseKCalorie, currentWeight);

        // 일일섭취목표 계산
        double dailyKCalorieGoal = calculateDailyKCalorieGoal(userId); // 일일섭취목표
        double remainingKCalorie = dailyKCalorieGoal - totalKCalorie + exerciseKCalorie;

        return new HomeSummaryResponse(
                dailyKCalorieGoal, // 일일섭취목표
                totalKCalorie,
                currentWeight,
                remainingKCalorie
        );
    }

    private double getExerciseKCalorie(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay, double exerciseKCalorie, double currentWeight) {
        List<Long> routineIds = routineRepository.findIdsByUserIdAndRoutineTypeAndCreatedAtBetween(userId, RoutineType.RECORD, startOfDay, endOfDay);
        if (!routineIds.isEmpty()) {
            List<RoutineExercise> rexList = new ArrayList<>();
            for (Long routineId : routineIds) {
                rexList = reRepository.findAllByRoutineId(routineId);
            }

            for (RoutineExercise re : rexList) {
                double met = (re.getExercise() != null && re.getExercise().getMetValue() != null)
                        ? re.getExercise().getMetValue() : 0.0;

                int minutes = 0;
                RoutineDetail detail = re.getRoutineDetail();
                if (detail != null && detail.getTime() != null) {
                    minutes = Math.max(0, detail.getTime());
                }

                exerciseKCalorie += kcalFromMet(met, currentWeight, minutes);
            }
        }
        return exerciseKCalorie;
    }

    private double kcalFromMet(double met, double currentWeight, int minutes) {
        // kcal = (MET * 3.5 * 체중(kg) / 200) * 운동시간(분)
        return (met * 3.5 * currentWeight / 200.0) * minutes;
    }

    // 일일섭취목표 칼로리 계산
    public double calculateDailyKCalorieGoal(Long userId) {
        // 일일섭취목표 : BMR - 일일감량목표칼로리

        //기초대사량 계산
        User user = userService.getUserById(userId);
        UserInformation userInfo = getUserInformationByUserId(userId);
        double weightValue = weightService.getLatestWeightByUserId(userId).getWeight();

        double activityConstant = userInfo.getActivity().getActivityConstant();
        double bmrWithActivity = user.getBMR(weightValue) * activityConstant;
        // 목표까지 감량해야 할 몸무게
        double TargetWeightLoss = Math.max(weightValue - user.getTargetWeight(), 0);
        int dietDays = userInfo.getDietVelocity().getPeriodInDays(); // 다이어트기간 '일'단위로 계산
        double dailyDeficit = (TargetWeightLoss * 7700) / dietDays; // 감량칼로리 / 다이어트기간
        return bmrWithActivity - dailyDeficit;
    }

    @Transactional(readOnly = true)
    public UserInformation getUserInformationByUserId(long userId) {
        return userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
    }
}
