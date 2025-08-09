package org.example.kuit_kac.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 현재 로그인한 사용자를 나타내는 인증주체 model
@Getter
@RequiredArgsConstructor
public class UserPrincipal {
    private final Long userId;
    private final String role; // "ROLE_USER"
    private final boolean termsAgreed; // 사용자 약관 동의 여부
    private final boolean onboardingNeeded; // 프로필 미입력 여부

    public static UserPrincipal from(User user, boolean termsAgreed) {
        boolean onboardingNeeded =
                (user.getGender() == null) ||
                        (user.getAge() == null) ||
                        (user.getHeight() == null) ||
                        (user.getTargetWeight() == null);
        return new UserPrincipal(user.getId(), "ROLE_USER", termsAgreed, onboardingNeeded);
    }
}
