package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietResponse;
import org.example.kuit_kac.domain.diet.dto.DietWithMealsAndFoodsResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.user.dto.UserResponse;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DietService {

    private final DietRepository dietRepository;

    public List<Diet> getDietsByUserId(Long userId) {
        List<Diet> diets = dietRepository.findByUserId(userId);
        if (diets.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return diets;
    }

    public List<Diet> getDietsByUserIdAndDietType(Long userId, DietType dietType) {
        List<Diet> diets = dietRepository.findByUserIdAndDietType(userId, dietType);
        if (diets.isEmpty()) {
            throw new CustomException(ErrorCode.DIET_USER_ID_AND_DIET_TYPE_NOT_FOUND);
        }
        return diets;
    }

    public DietWithMealsAndFoodsResponse getDietByUserIdAndDietTypeAndDietDate(Long userId, DietType dietType, LocalDate dietDate) {
        Diet diet = dietRepository.findByUserIdAndDietTypeAndDietDate(userId, dietType, dietDate)
                .orElseThrow(() -> new CustomException(ErrorCode.DIET_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND));
        return DietWithMealsAndFoodsResponse.from(diet);
    }
}