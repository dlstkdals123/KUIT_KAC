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
        User user = userRepository.findById(dietCreateRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        dietRepository.findByUserIdAndDietTypeAndDietDate(
                dietCreateRequest.getUserId(),
                dietCreateRequest.getDietType(),
                dietCreateRequest.getDietDate())
                .ifPresent(diet -> { throw new CustomException(ErrorCode.DIET_EXIST); });

        Diet diet = new Diet(user, dietCreateRequest.getDietType(), dietCreateRequest.getDietDate());

        if (ONLY_DIET_TYPES.contains(dietCreateRequest.getDietType())) {
            return createOnlyDiet(diet, dietCreateRequest);
        }
        return createDietWithMeals(diet, dietCreateRequest);
    }

    private Diet createOnlyDiet(Diet diet, DietCreateRequest dietCreateRequest) {
        if (dietCreateRequest.getMeals() != null && !dietCreateRequest.getMeals().isEmpty()) {
            throw new CustomException(ErrorCode.ONLY_DIET_CANNOT_CONTAIN_MEALS);
        }
        return dietRepository.save(diet);
    }

    private Diet createDietWithMeals(Diet diet, DietCreateRequest dietCreateRequest) {
        if (dietCreateRequest.getMeals() == null || dietCreateRequest.getMeals().isEmpty()) {
            throw new CustomException(ErrorCode.DIET_MEAL_EMPTY);
        }

        dietCreateRequest.getMeals().forEach(mealCreateRequest -> {
            Meal meal = mealService.createMealWithMealFoods(mealCreateRequest, diet);
            diet.addMeal(meal);
        });

        return dietRepository.save(diet);
    }
}