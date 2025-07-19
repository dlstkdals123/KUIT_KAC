package org.example.kuit_kac.domain.diet.repository;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    @EntityGraph(value = "Diet.withMeals")
    Optional<Diet> findByUserIdAndDietTypeAndDietDate(Long userId, DietType dietType, LocalDate dietDate);
}