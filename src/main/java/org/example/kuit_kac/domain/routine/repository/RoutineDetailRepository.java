package org.example.kuit_kac.domain.routine.repository;

import org.example.kuit_kac.domain.routine.model.RoutineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineDetailRepository extends JpaRepository<RoutineDetail, Long> {
}