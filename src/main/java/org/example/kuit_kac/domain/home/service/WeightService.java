package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.home.repository.WeightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeightService {
    private final WeightRepository weightRepository;

    // 1. 가장 최근 최중 조회
    @Transactional(readOnly = true)
    public Weight getLatestWeightByUserId(Long userId) {
        return weightRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(()-> new RuntimeException("최근 체중 정보가 없습니다."));
    }

    // 2. 오늘의 체중 저장 or 수정
    @Transactional
    public Weight saveOrUpdateTodayWeight(Long userId, double weightValue) {
        Optional<Weight> latestWeightOpt = weightRepository.findTopByUserIdOrderByCreatedAtDesc(userId);

        LocalDate today = LocalDate.now();

        if (latestWeightOpt.isPresent()) {
            Weight latest = latestWeightOpt.get();
            LocalDate latestDate = latest.getCreatedAt().toLocalDate();
            if (latestDate.isEqual(today)) {
                latest.setWeight(weightValue);
                return latest;
            }
        }
        // 오늘 데이터가 없거나 최신 데이터가 오늘이 아님 -> 새로 저장
        Weight newWeight = new Weight(userId, weightValue);
        return weightRepository.save(newWeight);
    }
}
