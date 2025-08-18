package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.diet_food.model.DietAifood;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.global.util.TimeRange;
import org.example.kuit_kac.global.util.TimeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.domain.diet_food.dto.DietAifoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodSnackCreateRequest;
import org.example.kuit_kac.domain.food.model.Aifood;
import org.example.kuit_kac.domain.food.service.FoodService;
import org.example.kuit_kac.domain.diet_food.service.DietAifoodService;

@Service
@RequiredArgsConstructor
public class DietService {

    private final DietRepository dietRepository;
    private final DietFoodService dietFoodService;
    private final DietAifoodService dietAifoodService;
    private final FoodService foodService;

    @Transactional(readOnly = true)
    public List<Diet> getDietsByUserId(Long userId, DietEntryType dietEntryType) {
        return dietRepository.findByUserIdAndDietEntryType(userId, dietEntryType);
    }

    @Transactional(readOnly = true)
    public List<Diet> getDietsByUserId(Long userId, DietType dietType) {
        return dietRepository.findByUserIdAndDietType(userId, dietType);
    }

    @Transactional(readOnly = true)
    public List<Diet> getPlansByUserIdAndDietDate(Long userId, LocalDate dietDate) {
        List<Diet> plans = dietRepository.findByUserIdAndDietEntryTypeAndDietDate(userId, DietEntryType.PLAN, dietDate);
        plans.addAll(dietRepository.findByUserIdAndDietEntryTypeAndDietDate(userId, DietEntryType.AI_PLAN, dietDate));
        return plans;
    }

    @Transactional(readOnly = true)
    public List<Diet> getPlansByUserIdBetweenDietDate(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Diet> plans = dietRepository.findByUserIdAndDietEntryTypeAndDietDateBetween(userId, DietEntryType.PLAN, startDate, endDate);
        plans.addAll(dietRepository.findByUserIdAndDietEntryTypeAndDietDateBetween(userId, DietEntryType.AI_PLAN, startDate, endDate));
        plans.addAll(dietRepository.findByUserIdAndDietEntryTypeAndDietDateBetween(userId, DietEntryType.DRINKING, startDate, endDate));
        plans.addAll(dietRepository.findByUserIdAndDietEntryTypeAndDietDateBetween(userId, DietEntryType.DINING_OUT, startDate, endDate));
        return plans;
    }

    @Transactional
    public Diet createTemplateDiet(User user, String name, List<DietFoodCreateRequest> foods) {
        // diet_type = TEMPLATE, diet_entry_type = null
        if (foods == null || foods.isEmpty()) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_MUST_HAVE_FOOD);
        }

        Diet diet = new Diet(user, name, DietType.TEMPLATE, null);
        Diet saved = dietRepository.save(diet);
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, saved, null);
        dietFoods.forEach(saved::addDietFood);
        return saved;
    }

    @Transactional
    public Diet updateTemplateDiet(Diet diet, String name, List<DietFoodCreateRequest> foods) {
        if (foods == null || foods.isEmpty()) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_MUST_HAVE_FOOD);
        }

        if (diet.getDietType() != DietType.TEMPLATE) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }

        diet.setName(name);
        diet.getDietFoods().clear();

        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, diet, null);
        dietFoods.forEach(diet::addDietFood);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createFastingDiet(User user, LocalDate dietDate, String dietTypeStr) {
        DietType dietType = DietType.getDietType(dietTypeStr);
        if (dietType == null || isTemplateDietType(dietType)) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }
        Diet diet = new Diet(user, null, dietDate, dietType, DietEntryType.FASTING);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createRecordDiet(User user, String name, String dietTypeStr, DietEntryType dietEntryType, LocalTime dietTime, List<DietFoodCreateRequest> foods) {
        DietType dietType = DietType.getDietType(dietTypeStr);

        // 1. 음식 유효성 먼저 체크
        if (foods == null || foods.isEmpty()) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_MUST_HAVE_FOOD);
        }

        // 2. dietType 검증
        if (dietType == null || isTemplateDietType(dietType)) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }

        // 모든 검증 통과 후 저장
        LocalDateTime dietDateTime = dietTime.atDate(LocalDate.now());
        Diet diet = new Diet(user, name, dietType, dietEntryType);
        Diet saved = dietRepository.save(diet);
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, saved, dietDateTime);
        dietFoods.forEach(saved::addDietFood);
        return saved;
    }

    @Transactional
    public Diet updateRecordDiet(Diet diet, String name, LocalTime dietTime, List<DietFoodCreateRequest> foods) {
        if (diet.getDietEntryType() != DietEntryType.RECORD) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_INVALID);
        }

        diet.setName(name);
        diet.getDietFoods().clear();
        LocalDateTime dietDateTime = dietTime.atDate(LocalDate.now());
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, diet, dietDateTime);
        dietFoods.forEach(diet::addDietFood);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createSnackDiet(User user, String name, String dietEntryTypeStr, List<DietFoodSnackCreateRequest> foods) {
        Diet diet = new Diet(user, name, DietType.SNACK, DietEntryType.getDietEntryType(dietEntryTypeStr));
        Diet saved = dietRepository.save(diet);
        List<DietFood> dietFoods = dietFoodService.createDietFoodsSnack(foods, saved);
        dietFoods.forEach(saved::addDietFood);
        return saved;
    }

    @Transactional
    public Diet updateSnackDiet(Diet diet, String name, List<DietFoodSnackCreateRequest> foods) {
        if (diet.getDietType() != DietType.SNACK) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }

        diet.setName(name);
        diet.getDietFoods().clear();
        List<DietFood> dietFoods = dietFoodService.createDietFoodsSnack(foods, diet);
        dietFoods.forEach(diet::addDietFood);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createPlanDiet(User user, String dietTypeStr, LocalDate date, List<DietFoodCreateRequest> foods) {
        LocalDateTime dietDateTime = TimeGenerator.getDateStart(date);
        Diet diet = new Diet(user, null, date, DietType.getDietType(dietTypeStr), DietEntryType.PLAN);
        Diet saved = dietRepository.save(diet);
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, saved, dietDateTime);
        dietFoods.forEach(saved::addDietFood);
        return saved;
    }

    @Transactional
    public Diet updatePlanDiet(Diet diet, LocalDate date, List<DietFoodCreateRequest> foods) {
        if (diet.getDietEntryType() != DietEntryType.PLAN) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_INVALID);
        }
        
        LocalDateTime dietDateTime = TimeGenerator.getDateStart(date);
        diet.getDietFoods().clear();
        diet.setDietDate(date);
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, diet, dietDateTime);
        dietFoods.forEach(diet::addDietFood);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createAiPlanDiet(User user, String dietTypeStr, LocalDate date, List<DietAifoodCreateRequest> aiDietFoods) {
        Map<DietAifoodCreateRequest, List<Aifood>> aifoodMap = aiDietFoods.stream()
                .collect(Collectors.toMap(
                    dietAifood -> dietAifood,
                    dietAifood -> dietAifood.aiFoods().stream()
                            .map(aifood -> foodService.createAifood(user, aifood))
                            .collect(Collectors.toList())
                ));

        LocalDateTime dietDateTime = TimeGenerator.getDateStart(date);
        Diet diet = new Diet(user, null, date, DietType.getDietType(dietTypeStr), DietEntryType.AI_PLAN);
        Diet saved = dietRepository.save(diet);
        List<DietAifood> dietAifoods = dietAifoodService.createAiDietFoodsWithDietTime(user, aifoodMap, saved, dietDateTime);
        dietAifoods.forEach(saved::addDietAifood);
        return dietRepository.save(saved);
    }

    @Transactional
    public void deleteDiet(Diet diet) {
        dietFoodService.deleteDietFoods(diet.getDietFoods());
        dietRepository.delete(diet);
    }

    private boolean isTemplateDietType(DietType dietType) {
        return dietType == DietType.TEMPLATE;
    }

    @Transactional(readOnly = true)
    public Diet getDietById(Long dietId) {
        return dietRepository.findById(dietId).orElseThrow(() -> new CustomException(ErrorCode.DIET_NOT_FOUND));
    }

    public long countDietFoodWithConditions(
            Long userId,
            DietEntryType entryType,
            boolean onlyLateNight, // 야식(밤 9시 ~ 새벽 3시) 필터링 여부
            TimeRange timeRange
    ) {
        List<DietFood> dietFoods = dietFoodService.getDietFoodsByDietIdAndTimeRange(
                userId, timeRange.start(), timeRange.end()
        );
        return dietFoods.stream()
                .filter(dietFood -> dietFood.getDiet() != null)
                // entryType == null(야식 필터링 요청) 경우 모든 식단의 시간 검사
                // entryType 지정될 경우 그 entryType의 식단만 검사
                .filter(dietFood -> entryType == null || dietFood.getDiet().getDietEntryType() == entryType)
                .filter(dietFood -> {
                    if (!onlyLateNight) return true;
                    int hour = dietFood.getDietTime().getHour();
                    return hour >= 21 || hour < 3;
                })
                .count();

    }
}