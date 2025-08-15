package org.example.kuit_kac.domain.terms.dto;

import lombok.Getter;
import org.example.kuit_kac.domain.terms.dto.TermCode;

@Getter
public class TermAgreementItem {
    private TermCode code;   // SERVICE_TOS / PRIVACY / MARKETING
    private String version;  // 동의한 문서 버전
    private boolean agreed;  // true=동의, false=철회
}
