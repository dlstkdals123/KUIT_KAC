package org.example.kuit_kac.domain.food.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "unit_type", nullable = false, length = 20)
    private String unitType;

    @Column(name = "unit_num", nullable = false)
    private Long unitNum;

    @Column(name = "food_type", nullable = false, length = 20)
    private String foodType;

    @Column(name = "is_processed_food", nullable = false)
    private Boolean isProcessedFood;

    @Column(nullable = true)
    private Double calorie;

    @Column(name = "carbohydrate_g", nullable = true)
    private Double carbohydrateG;

    @Column(name = "protein_g", nullable = true)
    private Double proteinG;

    @Column(name = "fat_g", nullable = true)
    private Double fatG;

    @Column(name = "sugar_g", nullable = true)
    private Double sugarG;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
