package org.example.kuit_kac.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "약관 업서트 요청")
public class TermAgreementUpsertRequest {
    @Schema(description = "약관 동의 항목 배열")
    private List<TermAgreementItem> agreements;
}
