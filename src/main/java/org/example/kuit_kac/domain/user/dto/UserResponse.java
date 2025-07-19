package org.example.kuit_kac.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user.model.User;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "사용자의 공개 프로필 및 기본 정보를 담는 응답 DTO입니다. 로그인, 조회, 등록 등의 API 응답에 사용됩니다.")
public class UserResponse {
    @Schema(description = "사용자의 고유 식별자 (ID)", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "사용자의 이름", example = "user1")
    private String nickname;

    @Schema(description = "사용자의 이메일", example = "user1@example.com")
    private String email;

    @Schema(description = "사용자의 성별", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"})
    private GenderType gender;

    @Schema(description = "사용자의 나이", example = "25")
    private int age;

    @Schema(description = "사용자의 키 (cm)", example = "175")
    private int height;

    @Schema(description = "사용자의 목표 체중 (kg)", example = "60.5")
    private double targetWeight;

    @Schema(description = "사용자 정보 생성일시", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "사용자 정보 최종 수정일시", example = "2023-01-01T10:00:00")
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getGender(),
                user.getAge(),
                user.getHeight(),
                user.getTargetWeight(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}