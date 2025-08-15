package org.example.kuit_kac.domain.terms.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TermAgreementResponse {
    private Long userId;
    private TermCode code;
    private String version;
    private boolean agreed;
    private LocalDateTime agreedAt;
    private LocalDateTime withdrawnAt;
}
