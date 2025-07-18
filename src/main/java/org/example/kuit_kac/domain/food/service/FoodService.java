package org.example.kuit_kac.domain.food.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Food getFoodByName(String name) {
        return foodRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Food> findAll() {
        return foodRepository.findAll();
    }
}
