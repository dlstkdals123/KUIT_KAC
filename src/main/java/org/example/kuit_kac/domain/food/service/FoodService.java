package org.example.kuit_kac.domain.food.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.food.dto.FoodResponse;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public FoodResponse getFoodById(@PathVariable Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        return FoodResponse.from(food);
    }
}
