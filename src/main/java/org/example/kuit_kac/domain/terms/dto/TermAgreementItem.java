package org.example.kuit_kac.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.terms.dto.TermCode;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "약관 항목", description = "각 약관 코드별 동의 여부")
public class TermAgreementItem {
    @Schema(description = "약관 코드", example = "SERVICE_TOS/PRIVACY/MARKETING")
    private TermCode code;   // SERVICE_TOS / PRIVACY / MARKETING

    @Schema(description = "약관 버전", example = "v1.0.0")
    private String version;  // 동의한 문서 버전

    @Schema(description = "동의 여부", example = "true")
    private boolean agreed = true;  // true=동의, false=철회
}
