package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietCreateRequest;
import org.example.kuit_kac.domain.diet.dto.DietResponse;
import org.example.kuit_kac.domain.diet.dto.DietWithMealsAndFoodsResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.dietTemplate.model.DietTemplate;
import org.example.kuit_kac.domain.dietTemplate.repository.DietTemplateRepository;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.domain.meal.dto.MealCreateRequest;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal_food.model.MealFood;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DietService {

    private final UserRepository userRepository;
    private final DietRepository dietRepository;
    private final DietTemplateRepository dietTemplateRepository;
    private final FoodRepository foodRepository;

    public DietWithMealsAndFoodsResponse getDietByUserIdAndDietTypeAndDietDate(Long userId, DietType dietType, LocalDate dietDate) {
        Diet diet = dietRepository.findByUserIdAndDietTypeAndDietDate(userId, dietType, dietDate)
                .orElseThrow(() -> new CustomException(ErrorCode.DIET_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND));
        return DietWithMealsAndFoodsResponse.from(diet);
    }

    @Transactional
    public DietWithMealsAndFoodsResponse createDiet(DietCreateRequest dietCreateRequest) {
        User user = userRepository.findById(dietCreateRequest.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        DietTemplate dietTemplate = Optional.ofNullable(dietCreateRequest.getDietTemplateId())
                .map(templateId -> dietTemplateRepository.findById(templateId)
                        .orElseThrow(() -> new CustomException(ErrorCode.DIET_TEMPLATE_NOT_FOUND)))
                .orElse(null);

        Diet diet = new Diet(user, dietTemplate, dietCreateRequest.getDietType(), dietCreateRequest.getName(), dietCreateRequest.getDietDate());

        if (dietCreateRequest.getDietType().equals(DietType.FASTING)) {
            return createFastingDiet(diet, dietCreateRequest);
        }
        return createNormalDiet(diet, dietCreateRequest);
    }

    private DietWithMealsAndFoodsResponse createFastingDiet(Diet diet, DietCreateRequest dietCreateRequest) {
        if (dietCreateRequest.getMeals() != null && !dietCreateRequest.getMeals().isEmpty()) {
            throw new CustomException(ErrorCode.FASTING_DIET_CANNOT_CONTAIN_MEALS);
        }
        Diet savedDiet = dietRepository.save(diet);
        return DietWithMealsAndFoodsResponse.from(savedDiet);
    }

    private DietWithMealsAndFoodsResponse createNormalDiet(Diet diet, DietCreateRequest dietCreateRequest) {
        if (dietCreateRequest.getMeals() == null || dietCreateRequest.getMeals().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_EMPTY);
        }
        List<Meal> meals = dietCreateRequest.getMeals().stream()
                .map(mealCreateRequest -> createMealWithFoods(mealCreateRequest, diet))
                .toList();

        Diet savedDiet = dietRepository.save(diet);
        return DietWithMealsAndFoodsResponse.from(savedDiet);
    }

    private Meal createMealWithFoods(MealCreateRequest mealCreateRequest, Diet diet) {
        if (mealCreateRequest.getMealFoods() == null || mealCreateRequest.getMealFoods().isEmpty()) {
            throw new CustomException(ErrorCode.MEAL_FOOD_EMPTY);
        }

        Meal meal = new Meal(diet, mealCreateRequest.getMealType(), mealCreateRequest.getMealTime());

        mealCreateRequest.getMealFoods().forEach(mealFoodRequest -> {
            Food food = foodRepository.findById(mealFoodRequest.getFoodId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

            MealFood mealFood = new MealFood(meal, food, mealFoodRequest.getQuantity());
            meal.addMealFood(mealFood);
        });
        return meal;
    }
}