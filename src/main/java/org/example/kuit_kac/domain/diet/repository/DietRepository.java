package org.example.kuit_kac.domain.diet.repository;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.model.DietType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByUserIdAndDietEntryType(Long userId, DietEntryType dietEntryType);
    List<Diet> findByUserIdAndDietType(Long userId, DietType dietType);
}
