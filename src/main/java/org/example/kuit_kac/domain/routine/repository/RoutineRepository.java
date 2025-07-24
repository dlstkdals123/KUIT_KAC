package org.example.kuit_kac.domain.routine.repository;

import org.example.kuit_kac.domain.routine.model.Routine;
import org.example.kuit_kac.domain.routine.model.RoutineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserIdAndRoutineTypeAndCreatedAtBetween(Long userId, RoutineType routineType, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Routine> findByUserIdAndRoutineType(Long userId, RoutineType routineType);
}