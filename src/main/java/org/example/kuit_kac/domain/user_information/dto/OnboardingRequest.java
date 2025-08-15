package org.example.kuit_kac.domain.user_information.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.home.model.DietVelocity;
import org.example.kuit_kac.domain.terms.dto.TermAgreementItem;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user_information.model.AppetiteType;
import org.example.kuit_kac.domain.user_information.model.EatingOutType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "온보딩 요청", description = "신규 사용자 온보딩 시 필요한 모든 필드")
public class OnboardingRequest {
    // kakaoId는 토큰에서만 가져오도록 함
    @Schema(description = "닉네임, 공백입력시 자동생성 가능", example = "user_dbd8b258")
    private String nickname;

    @Schema(description = "성별", example = "MALE")
    private GenderType gender;

    @Schema(description = "나이", example = "27")
    private Integer age;

    @Schema(description = "키(cm)", example = "175")
    private Integer height;

    @Schema(description = "목표 체중(kg)", example = "68.5")
    private Double targetWeight;

    @Schema(description = "다이어트 경험 여부", example = "true")
    private boolean hasDietExperience;

    @Schema(description = "다이어트 실패 이유", example = "의지 부족")
    private String dietFailReason;

    @Schema(description = "식욕 유형", example = "BIG, SMALL")
    private AppetiteType appetiteType;

    @Schema(description = "주간 외식 횟수", example = "4번 이상")
    private String weeklyEatingOutCount;

    @Schema(description = "외식 유형", example = "FAST_FOOD")
    private EatingOutType eatingOutType;

    @Schema(description = "다이어트 속도", example = "YUMYUM/COACH/ALL_IN")
    private DietVelocity dietVelocity;

    @Schema(description = "약관 동의 목록", nullable = true)
    private List<TermAgreementItem> agreements;
}

