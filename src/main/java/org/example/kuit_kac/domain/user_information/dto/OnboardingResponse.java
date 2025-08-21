package org.example.kuit_kac.domain.user_information.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.user.model.User;

@Getter
@AllArgsConstructor
@Schema(name = "온보딩 응답", description = "온보딩 후 유저 생성 및 bmr, 일일감량목표 반환")
public class OnboardingResponse {
    @Schema(description = "유저 아이디", example = "1")
    private long userId;
    @Schema(description = "BMR(기초대사량)", example = "1473")
    private int bmr;
    @Schema(description = "일일감량목표 칼로리", example = "1375")
    private int dailyDeficit;
    @Schema(description = "액세스 토큰", example = "eyJhbGciOi...")
    private String access;
    @Schema(description = "리프레쉬 토큰", example = "eyJhbGciOi...")
    private String refresh;
}
