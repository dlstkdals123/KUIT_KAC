package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietWithMealsAndFoodsResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DietService {

    private final DietRepository dietRepository;

    public DietWithMealsAndFoodsResponse getDietByUserIdAndDietTypeAndDietDate(Long userId, DietType dietType, LocalDate dietDate) {
        Diet diet = dietRepository.findByUserIdAndDietTypeAndDietDate(userId, dietType, dietDate)
                .orElseThrow(() -> new CustomException(ErrorCode.DIET_USER_ID_AND_DIET_TYPE_AND_DATE_NOT_FOUND));
        return DietWithMealsAndFoodsResponse.from(diet);
    }
}