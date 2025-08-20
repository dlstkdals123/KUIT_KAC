package org.example.kuit_kac.domain.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오 로그인 사용자 식별자
    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;
//
//    @Column(nullable = false, length = 100)
//    private String password;
//
//    @Column(nullable = false, unique = true, length = 50)
//    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType gender;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Double targetWeight;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User(String kakaoId, String nickname, GenderType gender, Integer age,
                Integer height, Double targetWeight) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.targetWeight = targetWeight;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public double getBMR(double weight) {
        if (gender == GenderType.MALE) {
            return 66.47 + (13.75 * weight) + (5 * height) - (6.76 * age);
        } else {
            return 655.1 + (9.56 * weight) + (1.85 * height) - (4.68 * age);
        }
    }
}


