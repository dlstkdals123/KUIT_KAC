package org.example.kuit_kac.domain.meal_food.repository;

import org.example.kuit_kac.domain.meal_food.model.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
    List<MealFood> findByMealId(Long mealId);
}