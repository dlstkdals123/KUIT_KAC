package org.example.kuit_kac.domain.user_information.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.home.model.DietVelocity;
import org.example.kuit_kac.domain.user_information.model.UserInformation;

@Getter
@AllArgsConstructor
public class UserInformationResponse {
    private long userId;
    private DietVelocity dietVelocity;

    public static UserInformationResponse from(UserInformation info) {
        return new UserInformationResponse(
                info.getUserId(),
                info.getDietVelocity()
        );
    }
}
