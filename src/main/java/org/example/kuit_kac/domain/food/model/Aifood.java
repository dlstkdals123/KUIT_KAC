package org.example.kuit_kac.domain.food.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.user.model.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "aifood")
public class Aifood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "unit_type", nullable = false, length = 20)
    private String unitType;

    @Column(name = "unit_num", nullable = false)
    private Long unitNum;

    @Column(name = "food_type", nullable = false, length = 20)
    private String foodType;

    @Column(name = "is_processed_food", nullable = false)
    private Boolean isProcessedFood = false;

    @Column(name = "calorie", nullable = false)
    private Double calorie = 0.0;

    @Column(name = "carbohydrate", nullable = false)
    private Double carbohydrate = 0.0;

    @Column(name = "protein", nullable = false)
    private Double protein = 0.0;

    @Column(name = "fat", nullable = false)
    private Double fat = 0.0;

    @Column(name = "sugar", nullable = false)
    private Double sugar = 0.0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 