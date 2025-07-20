package org.example.kuit_kac.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.global.util.EnumConverter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 생성 요청 DTO입니다.")
public class UserCreateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "사용자 비밀번호", example = "password123")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Schema(description = "사용자 이메일", example = "hong@example.com")
    private String email;

    @NotNull(message = "성별은 필수입니다.")
    @Schema(description = "사용자 성별", example = "남성", allowableValues = {"남성", "여성"})
    private String gender; // 한국어로 받음

    @NotNull(message = "나이는 필수입니다.")
    @Positive(message = "나이는 양수여야 합니다.")
    @Schema(description = "사용자 나이", example = "25")
    private Integer age;

    @NotNull(message = "키는 필수입니다.")
    @Positive(message = "키는 양수여야 합니다.")
    @Schema(description = "사용자 키 (cm)", example = "170")
    private Integer height;

    @NotNull(message = "목표 체중은 필수입니다.")
    @Positive(message = "목표 체중은 양수여야 합니다.")
    @Schema(description = "목표 체중 (kg)", example = "65.0")
    private Double targetWeight;

    // 한국어를 영어로 변환하는 메서드
    public org.example.kuit_kac.domain.user.model.GenderType getGenderType() {
        return EnumConverter.fromKoreanGender(this.gender);
    }
} 