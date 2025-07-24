package org.example.kuit_kac.domain.user_information.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.home.model.DietVelocity;
import org.example.kuit_kac.domain.user.model.User;

@Getter
@NoArgsConstructor
@Entity // JPA 테이블 매핑
@Table(name = "user_information")
public class UserInformation {
    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(name = "diet_velocity", nullable = false)
    private DietVelocity dietVelocity;

}
