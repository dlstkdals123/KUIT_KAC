package org.example.kuit_kac.domain.user_information.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.home.model.DietVelocity;
import org.example.kuit_kac.domain.user.model.User;

@Getter
@NoArgsConstructor
@Entity // JPA 테이블 매핑
public class UserInformation {
    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "has_diet_experience", nullable = false)
    private boolean hasDietExperience;

    @Column(name = "diet_fail_reason")
    private String dietFailReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "appetite_type")
    private AppetiteType appetiteType;

    @Column(name = "weekly_eating_out_count")
    private String weeklyEatingOutCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "eating_out_type")
    private EatingOutType eatingOutType;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_velocity", nullable = false)
    private DietVelocity dietVelocity;
}
