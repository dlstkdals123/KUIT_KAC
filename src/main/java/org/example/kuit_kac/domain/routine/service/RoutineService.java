package org.example.kuit_kac.domain.routine.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.routine.dto.RoutineDetailCreateRequest;
import org.example.kuit_kac.domain.routine.dto.RoutineExerciseCreateRequest;
import org.example.kuit_kac.domain.routine.model.Routine;
import org.example.kuit_kac.domain.routine.model.RoutineExercise;
import org.example.kuit_kac.domain.routine.model.RoutineType;
import org.example.kuit_kac.domain.routine.repository.RoutineRepository;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    private final RoutineExerciseService routineExerciseService;

    @Transactional(readOnly = true)
    public List<Routine> getRoutinesByUserIdBetween(Long userId, RoutineType routineType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return routineRepository.findByUserIdAndRoutineTypeAndCreatedAtBetween(userId, routineType, startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public List<Routine> getRoutinesByUserId(Long userId, RoutineType routineType) {
        return routineRepository.findByUserIdAndRoutineType(userId, routineType);
    }

    // @Transactional
    // public Diet createTemplateDiet(User user, String name, List<DietFoodCreateRequest> foods) {
    //     // diet_type = TEMPLATE, diet_entry_type = null
    //     if (foods == null || foods.isEmpty()) {
    //         throw new CustomException(ErrorCode.DIET_ENTRY_TYPE_MUST_HAVE_FOOD);
    //     }
    //     Diet diet = new Diet(user, name, DietType.TEMPLATE, null);
    //     Diet saved = dietRepository.save(diet);
    //     List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, saved, null);
    //     dietFoods.forEach(saved::addDietFood);
    //     return saved;
    // }

    // @Transactional
    // public Diet updateTemplateDiet(Diet diet, String name, List<DietFoodCreateRequest> foods) {
    //     diet.setName(name);
    //     diet.getDietFoods().clear();
    //     List<DietFood> dietFoods = dietFoodService.createDietFoodsWithDietTime(foods, diet, null);
    //     dietFoods.forEach(diet::addDietFood);
    //     return dietRepository.save(diet);
    // }

    @Transactional
    public Routine createGeneralRoutine(User user, String name, String routineTypeStr, List<RoutineExerciseCreateRequest> routineExercises) {
        // dietType과 dietEntryType이 모두 같은 값이 있으면 안됩니다.
        RoutineType routineType = RoutineType.getRoutineType(routineTypeStr);

        // entryType 검증
        if (routineType == null || isTemplateType(routineType)) {
            throw new CustomException(ErrorCode.ROUTINE_TYPE_INVALID);
        }

        // 모든 검증 통과 후 저장
        Routine routine = new Routine(user, name, routineType);
        Routine saved = routineRepository.save(routine);
        
        List<RoutineExercise> savedRoutineExercises = routineExerciseService.createRoutineExercises(routine, routineExercises);
        savedRoutineExercises.forEach(saved::addRoutineExercise);

        return saved;
    }

    @Transactional
    public Routine updateGeneralRoutine(Routine routine, String name, List<RoutineExerciseCreateRequest> routineExercises) {
        routine.setName(name);
        routine.getRoutineExercises().clear();
        List<RoutineExercise> savedRoutineExercises = routineExerciseService.createRoutineExercises(routine, routineExercises);
        savedRoutineExercises.forEach(routine::addRoutineExercise);
        return routineRepository.save(routine);
    }

    @Transactional
    public Routine createSimpleRoutine(User user, RoutineDetailCreateRequest aerobicDetail, RoutineDetailCreateRequest anaerobicDetail) {
        Routine routine = new Routine(user, null, RoutineType.RECORD);
        Routine saved = routineRepository.save(routine);
        List<RoutineExercise> savedRoutineExercises = routineExerciseService.createSimpleRoutineExercises(routine, aerobicDetail, anaerobicDetail);
        savedRoutineExercises.forEach(saved::addRoutineExercise);
        return saved;
    }

    @Transactional
    public void deleteRoutine(Routine routine) {
        routineExerciseService.deleteRoutineExercises(routine.getRoutineExercises());
        routineRepository.delete(routine);
    }

    private boolean isTemplateType(RoutineType routineType) {
        return routineType == RoutineType.TEMPLATE;
    }

    @Transactional(readOnly = true)
    public Routine getRoutineById(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() -> new CustomException(ErrorCode.ROUTINE_NOT_FOUND));
    }

}