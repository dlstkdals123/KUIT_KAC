package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodSnackCreateRequest;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;

@Service
@RequiredArgsConstructor
public class DietService {

    private final DietRepository dietRepository;
    private final DietFoodService dietFoodService;

    @Transactional(readOnly = true)
    public List<Diet> getDietsByUserId(Long userId, DietEntryType dietEntryType) {
        return dietRepository.findByUserIdAndDietEntryType(userId, dietEntryType);
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
        diet.setName(name);
        diet.getDietFoods().clear();
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, diet, null);
        dietFoods.forEach(diet::addDietFood);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createSimpleDiet(User user, String dietTypeStr, String dietEntryTypeStr) {
        // diet_entry_type = FASTING, DINING_OUT, DRINKING, name=null, foods=null
        DietEntryType entryType = DietEntryType.getDietEntryType(dietEntryTypeStr);
        if (entryType == null || !isSimpleDietEntryType(entryType)) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_INVALID);
        }
        DietType dietType = DietType.getDietType(dietTypeStr);
        if (dietType == null) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }
        Diet diet = new Diet(user, null, dietType, entryType);
        return dietRepository.save(diet);
    }

    @Transactional
    public Diet createGeneralDiet(User user, String name, String dietTypeStr, String dietEntryTypeStr, LocalDateTime dietTime, List<DietFoodCreateRequest> foods) {
        // dietType과 dietEntryType이 모두 같은 값이 있으면 안됩니다.
        DietEntryType entryType = DietEntryType.getDietEntryType(dietEntryTypeStr);
        DietType dietType = DietType.getDietType(dietTypeStr);

        // 1. 음식 유효성 먼저 체크
        if (foods == null || foods.isEmpty()) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_MUST_HAVE_FOOD);
        }

        // 2. entryType 검증
        if (entryType == null || isSimpleDietEntryType(entryType)) {
            throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_INVALID);
        }

        // 3. dietType 검증
        if (dietType == null || isTemplateDietType(dietType)) {
            throw new CustomException(ErrorCode.DIET_TYPE_INVALID);
        }

        // 4. 모든 검증 통과 후 저장
        Diet diet = new Diet(user, name, dietType, entryType);
        Diet saved = dietRepository.save(diet);
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, saved, dietTime);
        dietFoods.forEach(saved::addDietFood);
        return saved;
    }

    @Transactional
    public Diet updateGeneralDiet(Diet diet, String name, LocalDateTime dietTime, List<DietFoodCreateRequest> foods) {
        diet.setName(name);
        diet.getDietFoods().clear();
        List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, diet, dietTime);
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

    private boolean isSimpleDietEntryType(DietEntryType entryType) {
        return entryType == DietEntryType.FASTING || entryType == DietEntryType.DINING_OUT || entryType == DietEntryType.DRINKING;
    }

    private boolean isTemplateDietType(DietType dietType) {
        return dietType == DietType.TEMPLATE;
    }

    @Transactional(readOnly = true)
    public Diet getDietById(Long dietId) {
        return dietRepository.findById(dietId).orElseThrow(() -> new CustomException(ErrorCode.DIET_NOT_FOUND));
    }

}