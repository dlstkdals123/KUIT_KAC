package org.example.kuit_kac.domain.terms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TermAgreementUpsertRequest {
    private List<TermAgreementItem> agreements;
}
