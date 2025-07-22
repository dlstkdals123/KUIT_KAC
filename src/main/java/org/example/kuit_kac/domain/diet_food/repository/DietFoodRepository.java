package org.example.kuit_kac.domain.diet_food.repository;

import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, Long> {
    List<DietFood> findByDietIdAndDietTimeBetween(Long dietId, LocalDateTime startTime, LocalDateTime endTime);
}