package org.example.kuit_kac.domain.user_information.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.user_information.model.DietVelocity;
import org.example.kuit_kac.domain.user_information.model.UserInformation;

@Getter
@AllArgsConstructor
public class OnboardingResponse {
    private long userId;
    private DietVelocity dietVelocity;

    public static OnboardingResponse from(UserInformation info) {
        return new OnboardingResponse(
                info.getUserId(),
                info.getDietVelocity()
        );
    }
}
