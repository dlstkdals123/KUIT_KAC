package org.example.kuit_kac.domain.meal.repository;

import org.example.kuit_kac.domain.meal.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
}
