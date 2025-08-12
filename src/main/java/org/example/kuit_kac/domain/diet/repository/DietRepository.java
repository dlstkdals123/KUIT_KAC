package org.example.kuit_kac.domain.diet.repository;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.kuit_kac.domain.diet.model.DietType;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByUserId(Long userId);

    List<Diet> findByDietType(DietType dietType);

    List<Diet> findByUserIdAndDietType(Long userId, DietType dietType);

    List<Diet> findByUserIdAndDietEntryType(Long userId, DietEntryType dietEntryType);

    List<Diet> findByUserIdAndDietTypeAndDietEntryType(Long userId, DietType dietType, DietEntryType dietEntryType);
}
