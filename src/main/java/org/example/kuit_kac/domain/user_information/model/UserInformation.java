package org.example.kuit_kac.domain.user_information.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.home.model.DietVelocity;
import org.example.kuit_kac.domain.user.model.User;

@Getter
@Entity // JPA 테이블 매핑
@NoArgsConstructor
public class UserInformation {
    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "has_diet_experience", nullable = false)
    private boolean hasDietExperience;

    @Column(name = "diet_fail_reason", nullable = false)
    private String dietFailReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "appetite_type", nullable = false)
    private AppetiteType appetiteType;

    @Column(name = "weekly_eating_out_count", nullable = false)
    private String weeklyEatingOutCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "eating_out_type", nullable = false)
    private EatingOutType eatingOutType;

    public UserInformation(User user,
                           boolean hasDietExperience,
                           String dietFailReason,
                           AppetiteType appetiteType,
                           String weeklyEatingOutCount,
                           EatingOutType eatingOutType,
                           DietVelocity dietVelocity) {
        this.user = user;
        this.hasDietExperience = hasDietExperience;
        this.dietFailReason = dietFailReason;
        this.appetiteType = appetiteType;
        this.weeklyEatingOutCount = weeklyEatingOutCount;
        this.eatingOutType = eatingOutType;
        this.dietVelocity = dietVelocity;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_velocity", nullable = false)
    private DietVelocity dietVelocity;
}
