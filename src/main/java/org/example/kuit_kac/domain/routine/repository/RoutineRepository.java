package org.example.kuit_kac.domain.routine.repository;

import org.example.kuit_kac.domain.routine.model.Routine;
import org.example.kuit_kac.domain.routine.model.RoutineType;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserIdAndRoutineTypeAndCreatedAtBetween(Long userId, RoutineType routineType, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Routine> findByUserIdAndRoutineType(Long userId, RoutineType routineType);

    @Query("""
            select r.id
            from Routine r
            where r.user.id = :userId
              and r.routineType = :type
              and r.createdAt between :start and :end
            """)
    List<Long> findIdsByUserIdAndRoutineTypeAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("type") RoutineType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Modifying
    void deleteAllByUserId(Long userId);

}