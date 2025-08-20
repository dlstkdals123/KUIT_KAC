package org.example.kuit_kac.domain.user_information.repository;

public interface OnboardingStatusPort {
    boolean isOnboardingRequired(Long userId);
}
