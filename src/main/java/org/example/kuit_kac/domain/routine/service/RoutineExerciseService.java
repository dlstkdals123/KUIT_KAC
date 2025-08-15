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
    private final RoutineDetailService routineDetailService;
    private final RoutineSetService routineSetService;
    private final ExerciseService exerciseService;

    public List<RoutineExercise> createRoutineExercises(Routine routine, List<RoutineExerciseCreateRequest> routineExerciseRequests) {
        List<RoutineExercise> routineExercises = routineExerciseRequests.stream()
                .map(routineExerciseReq -> {
                    Exercise exercise = exerciseService.getExerciseById(routineExerciseReq.exerciseId());
                    RoutineExercise routineExercise = new RoutineExercise(routine, exercise);
                    
                    // Save the RoutineExercise first to get the ID
                    RoutineExercise savedRoutineExercise = routineExerciseRepository.save(routineExercise);
                    
                    // Create RoutineDetail (1개)
                    routineDetailService.createRoutineDetail(savedRoutineExercise, routineExerciseReq.routineDetail());
                    
                    // Create RoutineSets (여러 개)
                    routineSetService.createRoutineSets(savedRoutineExercise, routineExerciseReq.routineSets());
                    
                    return savedRoutineExercise;
                })
                .collect(Collectors.toList());

        return routineExercises;
    }

    public void deleteRoutineExercises(List<RoutineExercise> routineExercises) {
        routineExerciseRepository.deleteAll(routineExercises);
    }
}
