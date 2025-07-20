package org.example.kuit_kac.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.user.model.User;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "사용자 정보를 담는 응답 DTO입니다.")
public class UserResponse {
    @Schema(description = "사용자의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "사용자 이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "사용자 성별", example = "남성")
    private String gender;

    @Schema(description = "사용자 나이", example = "25")
    private Integer age;

    @Schema(description = "사용자 키 (cm)", example = "170")
    private Integer height;

    @Schema(description = "목표 체중 (kg)", example = "65.0")
    private Double targetWeight;

    @Schema(description = "사용자 정보 생성일시", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "사용자 정보 최종 수정일시", example = "2023-01-01T00:00:00")
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getGender().getKoreanName(),
                user.getAge(),
                user.getHeight(),
                user.getTargetWeight(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}