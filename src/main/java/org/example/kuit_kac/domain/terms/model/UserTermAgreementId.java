package org.example.kuit_kac.domain.terms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.TermCode;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserTermAgreementId implements Serializable {
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 32, nullable = false)
    private TermCode code;
}
