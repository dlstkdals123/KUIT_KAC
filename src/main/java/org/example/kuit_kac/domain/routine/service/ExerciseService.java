package org.example.kuit_kac.domain.routine.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.example.kuit_kac.domain.routine.model.Exercise;
import org.example.kuit_kac.domain.routine.repository.ExerciseRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXERCISE_NOT_FOUND));
    }
}
