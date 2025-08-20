package org.example.kuit_kac.domain.food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.repository.AifoodRepository;
import org.example.kuit_kac.domain.food.repository.FoodRepository;
import org.example.kuit_kac.domain.food.dto.AifoodCreateRequest;
import org.example.kuit_kac.domain.food.model.Aifood;
import org.example.kuit_kac.domain.food.model.FoodType;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final AifoodRepository aifoodRepository;

    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }

    @Transactional
    public Aifood createAifood(User user, AifoodCreateRequest request) {
        Aifood aifood = new Aifood(user, request.name(), request.unitType(), request.unitNum(), FoodType.getFoodType(request.foodType()), request.isProcessedFood(), request.kcal(), request.carb(), request.protein(), request.fat(), request.sugar(), request.score());
        return aifoodRepository.save(aifood);
    }

    public List<Aifood> getAifoodsByUserId(Long userId) {
        return aifoodRepository.findByUserId(userId);
    }

    public List<Food> getFoodsAfter(Long id) {
        return foodRepository.findByIdGreaterThanOrderByIdAsc(id);
    }
}
