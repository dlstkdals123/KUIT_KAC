package org.example.kuit_kac.domain.routine.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.routine.dto.RoutineExerciseCreateRequest;
import org.example.kuit_kac.domain.routine.model.Exercise;
import org.example.kuit_kac.domain.routine.model.Routine;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;
import org.example.kuit_kac.domain.routine.repository.RoutineExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {

    private final RoutineExerciseRepository routineExerciseRepository;
    private final ExerciseService exerciseService;

    public List<RoutineExercise> createRoutineExercises(Routine routine, List<RoutineExerciseCreateRequest> routineExerciseRequests) {
        List<RoutineExercise> routineExercises = routineExerciseRequests.stream()
                .map(routineExerciseReq -> {
                    Exercise exercise = exerciseService.getExerciseById(routineExerciseReq.exerciseId());
                    return new RoutineExercise(routine, exercise);
                })
                .collect(Collectors.toList());
        return routineExerciseRepository.saveAll(routineExercises);
    }

    public void deleteRoutineExercises(List<RoutineExercise> routineExercises) {
        routineExerciseRepository.deleteAll(routineExercises);
    }
}
