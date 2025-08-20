package org.example.kuit_kac.domain.user_information.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user_information.repository.OnboardingStatusPort;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingStatusService implements OnboardingStatusPort {
    private final UserInfoRepository userInfoRepository;
    public boolean isOnboardingRequired(Long userId) {
        if (userId == null) return true;
        return !userInfoRepository.existsById(userId);
    }
}
