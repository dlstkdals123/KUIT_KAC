package org.example.kuit_kac.domain.home.controller;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.home.dto.HomeWeightRequest;
import org.example.kuit_kac.domain.home.dto.HomeWeightResponse;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.home.service.WeightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home/weight")
@RequiredArgsConstructor
public class WeightController {
    private final WeightService weightService;

    @GetMapping("/{userId}")
    public ResponseEntity<HomeWeightResponse> getLatestWeight(@PathVariable Long userId) {
        Weight weight = weightService.getLatestWeightByUserId(userId)
                .orElseThrow(() -> new RuntimeException("체중 기록 없음")); // 기록없을경우
        return ResponseEntity.ok(HomeWeightResponse.from(weight));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateWeight(@PathVariable Long userId,
                                             @RequestBody HomeWeightRequest request) {
        weightService.saveOrUpdateTodayWeight(userId, request.getWeight());
        return ResponseEntity.ok().build();
    }
}
