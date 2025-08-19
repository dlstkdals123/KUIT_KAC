package org.example.kuit_kac.domain.home.repository;

import org.example.kuit_kac.domain.home.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {
    Optional<Weight> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    @Modifying
    void deleteAllByUserId(Long userId);
}
