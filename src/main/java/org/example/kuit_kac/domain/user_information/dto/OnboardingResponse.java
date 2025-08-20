package org.example.kuit_kac.domain.user_information.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.user.model.User;

@Getter
@AllArgsConstructor
public class OnboardingResponse {
    private long userId;
    private int bmr;
    private int dailyDeficit;

    public static OnboardingResponse from(User user, double bmr, double dailyDeficit) {
        return new OnboardingResponse(user.getId(), (int) bmr, (int) dailyDeficit);
    }

    public static OnboardingResponse from(long userId, double bmr, double dailyDeficit) {
        return new OnboardingResponse(userId, (int) bmr, (int) dailyDeficit);
    }
}
