package org.example.kuit_kac.domain.user_information.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.home.model.DietVelocity;

@Getter
@NoArgsConstructor
@Entity // JPA 테이블 매핑
@Table(name = "user_information")
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_velocity", nullable = false)
    private DietVelocity dietVelocity;

}
