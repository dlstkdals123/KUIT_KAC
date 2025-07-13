package org.example.kuit_kac.domain.meal.repository;

import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserId(Long userId);
    List<Meal> findByMealType(MealType mealType);
    List<Meal> findByUserIdAndMealType(Long userId, MealType mealType);
}
