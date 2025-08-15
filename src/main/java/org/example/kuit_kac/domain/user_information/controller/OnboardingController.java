package org.example.kuit_kac.domain.user_information.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user_information.dto.OnboardingRequest;
import org.example.kuit_kac.domain.user_information.service.OnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding")
public class OnboardingController {
    private final OnboardingService onboardingService;
    @PostMapping
    public ResponseEntity<Map<String, Object>> onboarding(@RequestBody @Valid OnboardingRequest request) {
        Long userId = onboardingService.createUserWithOnboarding(request);
        return ResponseEntity.ok(Map.of("userId", userId));
    }
}
