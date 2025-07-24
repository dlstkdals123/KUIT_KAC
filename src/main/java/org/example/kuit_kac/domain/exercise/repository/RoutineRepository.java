package org.example.kuit_kac.domain.exercise.repository;

import org.example.kuit_kac.domain.exercise.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
}