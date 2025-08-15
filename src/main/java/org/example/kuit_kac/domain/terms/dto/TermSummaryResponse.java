package org.example.kuit_kac.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "약관 요약 응답 DTO", description = "필수 약관 동의 여부 포함")
public class TermSummaryResponse {
    @Schema(description = "사용자 ID", example = "3")
    private Long userId;

    @Schema(description = "서비스 이용약관 동의 여부", example = "true")
    private boolean serviceTosAgreed;

    @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
    private boolean privacyAgreed;

    @Schema(description = "마케팅 수신 동의 여부", example = "false")
    private boolean marketingAgreed; // 선택

    @Schema(description = "필수 약관(SERVICE_TOS, PRIVACY) 모두 동의 여부", example = "true")
    private boolean requiredAllAgreed; // SERVICE_TOS & PRIVACY 둘 다 true
}
