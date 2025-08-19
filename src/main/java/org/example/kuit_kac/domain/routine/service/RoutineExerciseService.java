package org.example.kuit_kac.domain.routine.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.routine.dto.RoutineDetailCreateRequest;
import org.example.kuit_kac.domain.routine.dto.RoutineExerciseCreateRequest;
import org.example.kuit_kac.domain.routine.model.Exercise;
import org.example.kuit_kac.domain.routine.model.Routine;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;
import org.example.kuit_kac.domain.routine.repository.RoutineExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<RoutineExercise> createSimpleRoutineExercises(Routine routine, RoutineDetailCreateRequest aerobicDetail, RoutineDetailCreateRequest anaerobicDetail) {
        RoutineExercise aerobicRoutineExercise = new RoutineExercise(routine, exerciseService.getExerciseById(1L));
        RoutineExercise anaerobicRoutineExercise = new RoutineExercise(routine, exerciseService.getExerciseById(2L));
        List<RoutineExercise> routineExercises = new ArrayList<>();
        RoutineExercise savedAerobicRoutineExercise = routineExerciseRepository.save(aerobicRoutineExercise);
        RoutineExercise savedAnaerobicRoutineExercise = routineExerciseRepository.save(anaerobicRoutineExercise);
        routineExercises.add(savedAerobicRoutineExercise);
        routineExercises.add(savedAnaerobicRoutineExercise);
        routineDetailService.createRoutineDetail(savedAerobicRoutineExercise, aerobicDetail);
        routineDetailService.createRoutineDetail(savedAnaerobicRoutineExercise, anaerobicDetail);
        return routineExercises;
    }

    public void deleteRoutineExercises(List<RoutineExercise> routineExercises) {
        routineExerciseRepository.deleteAll(routineExercises);
    }
}
