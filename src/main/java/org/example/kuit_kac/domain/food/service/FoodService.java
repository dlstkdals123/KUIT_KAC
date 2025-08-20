package org.example.kuit_kac.domain.food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.domain.ai.dto.AiGenerateResponse.FoodItem;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }

    public List<Food> getFoodsAfter(Long id) {
        return foodRepository.findByIdGreaterThanOrderByIdAsc(id);
    }

    public Food createFood(FoodItem foodItem) {
        Optional<Food> food = foodRepository.findByName(foodItem.name());
        if (food.isPresent()) {
            return food.get();
        }
        Food newFood = Food.from(foodItem);
        return foodRepository.save(newFood);
    }
}
