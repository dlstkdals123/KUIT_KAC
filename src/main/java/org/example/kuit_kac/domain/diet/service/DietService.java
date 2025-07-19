package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietCreateRequest;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal.service.MealService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class DietService {

    private static final Set<DietType> ONLY_DIET_TYPES = EnumSet.of(
            DietType.FASTING,
            DietType.DINING_OUT,
            DietType.DRINKING
    );

    private final UserRepository userRepository;
    private final DietRepository dietRepository;

    private final MealService mealService;


    @Transactional(readOnly = true)
    public Diet getDietByUserIdAndDietTypeAndDietDate(Long userId, DietType dietType, LocalDate dietDate) {
        return dietRepository.findByUserIdAndDietTypeAndDietDate(userId, dietType, dietDate)
                .orElseThrow(() -> new CustomException(ErrorCode.DIET_BY_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Diet getDietById(Long id) {
        return dietRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIET_NOT_FOUND));
    }

    @Transactional
    public Diet createDiet(DietCreateRequest dietCreateRequest) {
        // 사용자 존재 여부 확인
        User user = userRepository.findById(dietCreateRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 사용자, 식단 유형, 날짜가 모두 동일한 식단이 존재하는지 확인
        dietRepository.findByUserIdAndDietTypeAndDietDate(
                dietCreateRequest.getUserId(),
                dietCreateRequest.getDietType(),
                dietCreateRequest.getDietDate())
                .ifPresent(diet -> { throw new CustomException(ErrorCode.DIET_EXIST); });

        // RECORD인 경우 오늘 날짜의 식단만 추가할 수 있습니다.
        if (dietCreateRequest.getDietType() == DietType.RECORD && !dietCreateRequest.getDietDate().isEqual(LocalDate.now())) {
            throw new CustomException(ErrorCode.DIET_DATE_IS_NOT_TODAY);
        }

        // 식단 생성
        Diet diet = new Diet(user, dietCreateRequest.getDietType(), dietCreateRequest.getDietDate());

        // meal을 포함하지 않는 유형인 경우 Diet만 생성 ex) 단식, 외식, 술자리
        if (ONLY_DIET_TYPES.contains(dietCreateRequest.getDietType())) {
            return createOnlyDiet(diet, dietCreateRequest);
        }
        // meal을 포함하는 유형인 경우 Diet와 Meal을 생성 ex) 식단, 계획, AI 계획
        return createDietWithMeals(user, diet, dietCreateRequest);
    }

    // meal을 포함하지 않는 유형인 경우 Diet만 생성 ex) 단식, 외식, 술자리
    private Diet createOnlyDiet(Diet diet, DietCreateRequest dietCreateRequest) {
        if (dietCreateRequest.getMeals() != null && !dietCreateRequest.getMeals().isEmpty()) {
            throw new CustomException(ErrorCode.ONLY_DIET_CANNOT_CONTAIN_MEALS);
        }
        return dietRepository.save(diet);
    }

    // meal을 포함하는 유형인 경우 Diet와 Meal을 생성 ex) 식단, 계획, AI 계획
    private Diet createDietWithMeals(User user, Diet diet, DietCreateRequest dietCreateRequest) {
        if (dietCreateRequest.getMeals() == null || dietCreateRequest.getMeals().isEmpty()) {
            throw new CustomException(ErrorCode.DIET_MEAL_EMPTY);
        }

        dietCreateRequest.getMeals().forEach(mealCreateRequest -> {
            Meal meal = mealService.createMealWithMealFoods(mealCreateRequest, diet, user);
            diet.addMeal(meal);
        });

        return dietRepository.save(diet);
    }
}