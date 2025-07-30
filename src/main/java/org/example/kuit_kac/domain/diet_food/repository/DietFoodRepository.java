package org.example.kuit_kac.domain.diet_food.repository;

import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, Long> {
    // DietFood 엔티티를 기준으로, 연관된 Diet의 필드들(userId, dietEntryType)과 자기자신의 필드(dietTime) 조건 적용
    List<DietFood> findByDiet_UserIdAndDietTimeBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

}