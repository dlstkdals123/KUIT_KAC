package org.example.kuit_kac.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 현재 로그인한 사용자를 나타내는 인증주체 model
@Getter
@RequiredArgsConstructor
public class UserPrincipal {
    private final Long userId;
    private final String kakaoId;
    private final String role; // "ROLE_USER"
    private final boolean termsAgreed; // 사용자 약관 동의 여부
    private final boolean onboardingNeeded; // 프로필 미입력 여부

    public static
    UserPrincipal from(User user, boolean termsAgreed, String kakaoId) {
        boolean onboardingNeeded =
                (user == null ||
                        user.getGender() == null) ||
                        (user.getAge() == null) ||
                        (user.getHeight() == null) ||
                        (user.getTargetWeight() == null);
        return new UserPrincipal(
                user != null ? user.getId() : null,
                kakaoId,
                "ROLE_USER",
                termsAgreed,
                onboardingNeeded);
    }

    // ✅ DEV 우회용: 약관/온보딩을 항상 통과시키는 프린시펄
    public static UserPrincipal devBypass(Long userId, String kakaoId) {
        // 절대 null 금지: 음수로 고정(실 유저와 안 겹치게)
        long fakeUid = (userId == null ? -1L : userId);
        return new UserPrincipal(
                userId,
                kakaoId,
                "ROLE_USER",
                true,                 // termsAgreed = true
                false                 // onboardingNeeded = false
        );
    }
}
