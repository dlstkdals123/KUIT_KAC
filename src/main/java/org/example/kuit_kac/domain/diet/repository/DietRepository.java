package org.example.kuit_kac.domain.diet.repository;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByUserIdAndDietEntryTypeAndDietTimeBetween(Long userId, DietEntryType dietEntryType, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
