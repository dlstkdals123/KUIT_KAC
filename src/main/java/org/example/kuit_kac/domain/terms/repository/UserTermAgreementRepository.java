// src/main/java/org/example/kuit_kac/domain/terms/repository/UserTermAgreementRepository.java
package org.example.kuit_kac.domain.terms.repository;

import org.example.kuit_kac.domain.terms.dto.TermCode;
import org.example.kuit_kac.domain.terms.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface UserTermAgreementRepository extends JpaRepository<UserTermAgreement, UserTermAgreementId> {

    List<UserTermAgreement> findByIdUserId(Long userId);

    Optional<UserTermAgreement> findByIdUserIdAndIdCode(Long userId, TermCode code);

    @Query("""
      select count(a) > 0 from UserTermAgreement a
      where a.id.userId = :userId and a.id.code = :code and a.agreed = true
    """)
    boolean isAgreed(Long userId, TermCode code);
}
