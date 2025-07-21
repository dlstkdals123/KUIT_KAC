package org.example.kuit_kac.domain.diet.repository;

import org.example.kuit_kac.domain.diet.model.DietType;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByUserId(Long userId);

    List<Diet> findByDietType(DietType dietType);

    List<Diet> findByUserIdAndDietType(Long userId, DietType dietType);

    List<Diet> findByUserIdAndDietEntryTypeAndDietTimeBetween(Long userId, DietEntryType dietEntryType, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @EntityGraph(value = "Diet.withDietFoodsAndFood", type = EntityGraph.EntityGraphType.LOAD)
    List<Diet> findByUserIdAndDietTimeBetween(long userId, LocalDateTime start, LocalDateTime end);
}
