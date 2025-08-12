package org.example.kuit_kac.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.model.UserPrincipal;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "사용자 응답 DTO")
public class UserResponse {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "성별", example = "MALE")
    private GenderType gender;
    @Schema(description = "나이", example = "20")
    private Integer age;
    @Schema(description = "키(cm)", example = "170")
    private Integer height;
    @Schema(description = "목표 체중(kg)", example = "60.5")
    private Double targetWeight;

    @Schema(description = "약관 동의 여부", example = "true")
    private boolean termsAgreed;
    @Schema(description = "온보딩 필요 여부", example = "false")
    private boolean onboardingNeeded;

    public static UserResponse from(User user, boolean termsAgreed, boolean onboardingNeeded) {
        return UserResponse.builder()
                .userId(user.getId())
                .gender(user.getGender())
                .age(user.getAge())
                .height(user.getHeight())
                .targetWeight(user.getTargetWeight())
                .termsAgreed(termsAgreed)
                .onboardingNeeded(onboardingNeeded)
                .build();
    }

}
