package org.example.kuit_kac.domain.diet.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DietService {

    private final DietRepository dietRepository;

    @Transactional(readOnly = true)
    public List<Diet> getDietsByUserId(Long userId, DietEntryType dietEntryType, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59
        List<Diet> diets = dietRepository.findByUserIdAndDietEntryTypeAndDietTimeBetween(userId, dietEntryType, startOfDay, endOfDay);
        return diets;
    }
}