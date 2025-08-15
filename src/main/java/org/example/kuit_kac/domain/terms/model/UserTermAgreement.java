package org.example.kuit_kac.domain.terms.model;

// src/main/java/org/example/kuit_kac/domain/terms/model/UserTermAgreement.java
import com.nimbusds.oauth2.sdk.device.UserCode;
import jakarta.persistence.*;
import lombok.*;
import org.example.kuit_kac.domain.terms.dto.TermCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_term_agreement")
public class UserTermAgreement {

    @EmbeddedId
    private UserTermAgreementId id;

    @Column(length = 32, nullable = false)
    private String version;           // 동의한 문서 버전

    @Column(nullable = false)
    private boolean agreed;           // 동의 여부 (false면 철회 상태로 간주)

    private LocalDateTime agreedAt;   // 동의 시점 (agreed=true이면 세팅)
    private LocalDateTime withdrawnAt;// 철회 시점 (agreed=false로 바뀌는 순간 세팅)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 편의 메서드
    public void agree(String version, LocalDateTime now) {
        this.version = version;
        this.agreed = true;
        this.agreedAt = now;
        this.withdrawnAt = null;
    }

    public void withdraw(LocalDateTime now) {
        this.agreed = false;
        this.withdrawnAt = now;
    }
}

