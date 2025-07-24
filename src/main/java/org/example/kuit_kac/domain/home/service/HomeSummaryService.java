package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.home.dto.HomeSummaryResponse;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.home.repository.WeightRepository;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInformationRepository;
import org.example.kuit_kac.domain.user_information.service.UserInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// TODO: 컨트롤러로 로직분리
public class HomeSummaryService {
    private final DietService dietService;
    private final DietRepository dietRepository;
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final WeightService weightService;

    // 하루 섭취 영양소 요약
    @Transactional(readOnly = true)
    public HomeSummaryResponse getTodayHomeSummary(long userId, LocalDate date) {
        /*
        TODO 가져와야하는 정보: 일일섭취목표 - 오늘 먹은 음식들의 칼로리 합산 - 운동해서 소모한 칼로리 = 몇칼로리 남았는지 계산(음수 반환 가능)
         */

        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1); // 23:59:59

        // 날짜정보로 식단기록 가져오기
        List<Diet> diets = dietRepository.findByUserIdAndDietTimeBetween(userId, startOfDay, endOfDay);

        // 오늘 섭취 칼로리 계산
        double totalKCalorie = 0;
        for (Diet diet : diets) {
            for (DietFood dietFood : diet.getDietFoods()) {
                Food food = dietFood.getFood();
                double quantity = dietFood.getQuantity(); // 양 가져오기
                double unitCalorie = food.getCalorie(); // 단위당 칼로리 가져오기
                totalKCalorie += quantity * unitCalorie;
            }
        }



        return calculateDailyKCalorieGoal(userId)



    }

    // 소수점 둘째자리까지 반올림
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // 일일섭취목표 칼로리 계산
    public double calculateDailyKCalorieGoal(Long userId) {
        // 일일섭취목표 : BMR - 일일감량목표칼로리

        User user = userService.getUserById(userId);
        UserInformation userInfo = userInformationService.getUserInformationByUserId(userId);
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
