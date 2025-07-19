package org.example.kuit_kac.domain.meal_food.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.food.model.Aifood;
import org.example.kuit_kac.domain.meal.model.Meal;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "meal_aifood")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "MealAifood.withAifood", attributeNodes = @NamedAttributeNode("aifood"))
})
public class MealAifood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aifood_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Aifood aifood;

    @Column(nullable = false)
    private double quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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

    @Builder
    public MealAifood(Meal meal, Aifood aifood, double quantity) {
        this.meal = meal;
        this.aifood = aifood;
        this.quantity = quantity;
    }

    public void addAifood(Aifood aifood) {
        this.aifood = aifood;
    }
} 