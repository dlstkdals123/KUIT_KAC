package org.example.kuit_kac.domain.routine.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.routine.dto.RoutineSetCreateRequest;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;
import org.example.kuit_kac.domain.routine.model.RoutineSet;
import org.example.kuit_kac.domain.routine.repository.RoutineSetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineSetService {

    private final RoutineSetRepository routineSetRepository;

    public List<RoutineSet> createRoutineSets(RoutineExercise routineExercise, List<RoutineSetCreateRequest> routineSetRequests) {
        List<RoutineSet> routineSets = routineSetRequests.stream()
                .map(request -> new RoutineSet(
                    routineExercise,
                    request.count(),
                    request.weightKg(),
                    request.weightNum(),
                    request.distance(),
                    request.time(),
                    request.setOrder()
                ))
                .collect(Collectors.toList());
        List<RoutineSet> savedRoutineSets = routineSetRepository.saveAll(routineSets);
        routineExercise.setRoutineSets(savedRoutineSets);
        return savedRoutineSets;
    }

    public void deleteRoutineSets(List<RoutineSet> routineSets) {
        routineSetRepository.deleteAll(routineSets);
    }
}