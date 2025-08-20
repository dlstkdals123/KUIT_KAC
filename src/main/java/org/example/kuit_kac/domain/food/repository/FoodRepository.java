package org.example.kuit_kac.domain.food.repository;

import java.util.List;
import java.util.Optional;

import org.example.kuit_kac.domain.food.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);
    List<Food> findByIdGreaterThanOrderByIdAsc(Long id);
}
