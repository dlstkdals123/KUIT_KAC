package org.example.kuit_kac.domain.terms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TermSummaryResponse {
    private Long userId;
    private boolean serviceTosAgreed;
    private boolean privacyAgreed;
    private boolean marketingAgreed; // 선택
    private boolean requiredAllAgreed; // SERVICE_TOS & PRIVACY 둘 다 true
}
