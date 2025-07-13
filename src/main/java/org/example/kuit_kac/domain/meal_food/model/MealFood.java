package org.example.kuit_kac.domain.meal_food.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.meal.model.Meal;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "meal_food")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "MealFood.withFood", attributeNodes = @NamedAttributeNode("food"))
})
public class MealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Food food;

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
    public MealFood(Meal meal, Food food, double quantity) {
        this.meal = meal;
        this.food = food;
        this.quantity = quantity;
    }

    public void addFood(Food food) {
        this.food = food;
    }
}