package org.example.kuit_kac.domain.diet_food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DietFoodService {

    private final DietFoodRepository dietFoodRepository;

    public List<DietFood> getDietFoodsByDietIdAndTimeRange(Long dietId, LocalDateTime startTime, LocalDateTime endTime) {
        return dietFoodRepository.findByDietIdAndDietTimeBetween(dietId, startTime, endTime);
    }
}
