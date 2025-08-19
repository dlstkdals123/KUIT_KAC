package org.example.kuit_kac.domain.routine.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.routine.dto.RoutineDetailCreateRequest;
import org.example.kuit_kac.domain.routine.model.Intensity;
import org.example.kuit_kac.domain.routine.model.RoutineDetail;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;
import org.example.kuit_kac.domain.routine.repository.RoutineDetailRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineDetailService {

    private final RoutineDetailRepository routineDetailRepository;

    public RoutineDetail createRoutineDetail(RoutineExercise routineExercise, RoutineDetailCreateRequest routineDetailRequest) {
        Intensity intensity = Intensity.getIntensity(routineDetailRequest.intensity());
        RoutineDetail routineDetail = new RoutineDetail(routineExercise, routineDetailRequest.time(), intensity);
        RoutineDetail saved = routineDetailRepository.save(routineDetail);
        routineExercise.setRoutineDetail(saved);
        return saved;
    }

    public void deleteRoutineDetail(RoutineDetail routineDetail) {
        routineDetailRepository.delete(routineDetail);
    }
}
