package org.example.kuit_kac.domain.terms.repository;

import org.example.kuit_kac.domain.terms.dto.TermCode;
import org.example.kuit_kac.domain.terms.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface UserTermAgreementRepository extends JpaRepository<UserTermAgreement, UserTermAgreementId> {

    List<UserTermAgreement> findByIdUserId(Long userId);

    Optional<UserTermAgreement> findByIdUserIdAndIdCode(Long userId, TermCode code);

    @Query("""
              select count(a) > 0 from UserTermAgreement a
              where a.id.userId = :userId and a.id.code = :code and a.agreed = true
            """)
    boolean isAgreed(Long userId, TermCode code);

    boolean existsByIdUserIdAndIdCodeAndAgreedTrue(Long userId, TermCode code);

    @Modifying
    void deleteAllByIdUserId(Long userId);

    @Modifying
    @Query(value = """
              INSERT INTO user_term_agreement(user_id, code, version, agreed, agreed_at)
              VALUES (:uid, :code, :version, :agreed, CASE WHEN :agreed THEN NOW() ELSE NULL END)
              ON DUPLICATE KEY UPDATE
                version=VALUES(version),
                agreed=VALUES(agreed),
                agreed_at=CASE WHEN VALUES(agreed) THEN NOW() ELSE NULL END,
                updated_at=NOW()
            """, nativeQuery = true)
    int upsertAgreement(Long uid, String code, String version, boolean agreed);

}
