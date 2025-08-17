package org.example.kuit_kac.domain.user_information.repository;

import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInformation, Long> {
    Optional<UserInformation> findByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    @Modifying
    @Query(value = """
              INSERT INTO user_information(
                user_id, has_diet_experience, diet_fail_reason,
                appetite_type, weekly_eating_out_count, eating_out_type, diet_velocity
              )
              VALUES (:uid, :exp, :reason, :appetite, :count, :eatingType, :velocity)
              ON DUPLICATE KEY UPDATE
                has_diet_experience = VALUES(has_diet_experience),
                diet_fail_reason    = VALUES(diet_fail_reason),
                appetite_type       = VALUES(appetite_type),
                weekly_eating_out_count = VALUES(weekly_eating_out_count),
                eating_out_type     = VALUES(eating_out_type),
                diet_velocity       = VALUES(diet_velocity),
                updated_at          = NOW()
            """, nativeQuery = true)
    int upsertInfo(
            @Param("uid") Long uid,
            @Param("exp") boolean hasDietExperience,
            @Param("reason") String dietFailReason,          // null 허용
            @Param("appetite") String appetiteType,            // "SMALL" | "BIG" | null
            @Param("count") String weeklyEatingOutCount,    // 예: "1-2" | null
            @Param("eatingType") String eatingOutType,           // "FASTFOOD" | "KOREAN" | ... (NOT NULL)
            @Param("velocity") String dietVelocity             // "YUMYUM" | "COACH" | "ALL_IN" (NOT NULL)
    );
}
