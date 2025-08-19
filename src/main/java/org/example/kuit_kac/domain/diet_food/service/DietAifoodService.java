package org.example.kuit_kac.domain.diet_food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.diet_food.dto.DietAifoodCreateRequest;
import org.example.kuit_kac.domain.diet_food.model.DietAifood;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.food.model.Aifood;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.diet_food.repository.DietAifoodRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DietAifoodService {

    private final DietAifoodRepository dietAifoodRepository;

    public List<DietAifood> createAiDietFoodsWithDietTime(User user, Map<DietAifoodCreateRequest, List<Aifood>> aifoodMap, Diet diet, LocalDateTime dietTime) {
        List<DietAifood> dietAifoods = new ArrayList<>();
        aifoodMap.forEach((aiDietFood, aifoods) -> {
            aifoods.forEach(aifood -> {
                DietAifood dietAifood = new DietAifood(diet, aifood, aiDietFood.quantity(), dietTime);
                dietAifoodRepository.save(dietAifood);
                dietAifoods.add(dietAifood);
            });
        });
        return dietAifoods;
    }
}
