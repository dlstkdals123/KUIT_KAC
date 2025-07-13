package org.example.kuit_kac.domain.meal.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.meal_food.model.MealFood;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "meal")
@NamedEntityGraphs({
    // Meal.withMealFoods: mealFoods 필드를 페치
    @NamedEntityGraph(
        name = "Meal.withMealFoods",
        attributeNodes = @NamedAttributeNode("mealFoods")
    ),
    // Meal.withMealFoodsAndFood: mealFoods -> food 를 페치
    @NamedEntityGraph(
        name = "Meal.withMealFoodsAndFood",
        attributeNodes = @NamedAttributeNode(
            value = "mealFoods",
            subgraph = "mealFoodsSubgraph"
        ),
        subgraphs = @NamedSubgraph(
            name = "mealFoodsSubgraph",
            attributeNodes = @NamedAttributeNode("food")
        )
    )
})
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Diet diet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType mealType;

    @Column(nullable = false)
    private LocalDateTime mealTime;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealFood> mealFoods = new ArrayList<>();

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
    public Meal(Diet diet, MealType mealType, LocalDateTime mealTime) {
        this.diet = diet;
        this.mealType = mealType;
        this.mealTime = mealTime;
    }

    public void addMealFood(MealFood mealFood) {
        this.mealFoods.add(mealFood);
        mealFood.setMeal(this);
    }
}