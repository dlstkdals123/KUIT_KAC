package org.example.kuit_kac.domain.food.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.food.dto.FoodResponse;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    public List<FoodResponse> getAllFoods() {
        List<Food> foods = foodRepository.findAll();
        return foods.stream().map(FoodResponse::from).collect(Collectors.toList());
    }
}
