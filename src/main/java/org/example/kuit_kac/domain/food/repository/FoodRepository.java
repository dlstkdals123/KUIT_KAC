package org.example.kuit_kac.domain.food.repository;

import org.example.kuit_kac.domain.food.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
