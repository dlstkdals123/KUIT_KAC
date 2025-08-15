package org.example.kuit_kac.domain.routine.repository;

import org.example.kuit_kac.domain.routine.model.RoutineSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineSetRepository extends JpaRepository<RoutineSet, Long> {
}