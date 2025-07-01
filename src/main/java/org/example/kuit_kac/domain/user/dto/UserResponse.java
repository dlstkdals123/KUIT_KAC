package org.example.kuit_kac.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "사용자의 공개 프로필 및 기본 정보를 담는 응답 DTO입니다. 로그인, 조회, 등록 등의 API 응답에 사용됩니다.")
public class UserResponse {
    @Schema(description = "사용자의 고유 식별자 (ID)", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "사용자의 이름", example = "인상민")
    private String username;
}
