package org.example.kuit_kac.domain.diet_food.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.food.model.Food;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "diet_food")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "DietFood.withFood", attributeNodes = @NamedAttributeNode("food"))
})
public class DietFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Diet diet;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Food food;

    @Column(nullable = false)
    private double quantity;

    @Column(name = "diet_time", nullable = false)
    private LocalDateTime dietTime;

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

    public DietFood(Diet diet, Food food, double quantity, LocalDateTime dietTime) {
        this.diet = diet;
        this.food = food;
        this.quantity = quantity;
        this.dietTime = dietTime;
    }
}