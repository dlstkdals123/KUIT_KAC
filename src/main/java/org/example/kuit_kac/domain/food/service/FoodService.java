package org.example.kuit_kac.domain.food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("음식 없음"));
    }
}
