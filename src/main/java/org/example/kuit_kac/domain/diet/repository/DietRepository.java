package org.example.kuit_kac.domain.diet.repository;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.example.kuit_kac.domain.diet.model.DietType;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByUserId(Long userId);

    List<Diet> findByDietType(DietType dietType);

    List<Diet> findByUserIdAndDietType(Long userId, DietType dietType);

    List<Diet> findByUserIdAndDietEntryType(Long userId, DietEntryType dietEntryType);

    List<Diet> findByUserIdAndDietEntryTypeAndDietDate(Long userId, DietEntryType dietEntryType, LocalDate dietDate);

    List<Diet> findByUserIdAndDietEntryTypeAndDietDateBetween(Long userId, DietEntryType dietEntryType, LocalDate startDate, LocalDate endDate);

    List<Diet> findByUserIdAndDietTypeAndDietEntryType(Long userId, DietType dietType, DietEntryType dietEntryType);

    @Modifying
    void deleteAllByUserId(Long userId);
}
