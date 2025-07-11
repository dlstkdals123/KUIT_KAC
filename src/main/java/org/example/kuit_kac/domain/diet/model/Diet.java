package org.example.kuit_kac.domain.diet.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kuit_kac.domain.dietTemplate.model.DietTemplate;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.meal.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor()
@Entity
@NamedEntityGraphs({
    // Diet.withMeals: meals 필드를 페치 (단일 레벨)
    @NamedEntityGraph(
        name = "Diet.withMeals",
        attributeNodes = @NamedAttributeNode("meals") // attributePaths 대신 attributeNodes 사용
    ),
    // Diet.withMealsAndFoods: meals -> mealFoods -> food 를 페치 (중첩 레벨)
    @NamedEntityGraph(
        name = "Diet.withMealsAndFoods",
        attributeNodes = @NamedAttributeNode(
            value = "meals",
            subgraph = "mealsSubgraph" // meals 필드에 대한 서브그래프 정의
        ),
        subgraphs = {
            @NamedSubgraph( // "mealsSubgraph" 정의: Meal 엔티티 내부의 mealFoods 필드를 페치
                name = "mealsSubgraph",
                attributeNodes = @NamedAttributeNode(
                    value = "mealFoods", // Meal 엔티티의 mealFoods 필드
                    subgraph = "mealFoodsSubgraph" // mealFoods 필드에 대한 서브그래프 정의
                )
            ),
            @NamedSubgraph( // "mealFoodsSubgraph" 정의: MealFood 엔티티 내부의 food 필드를 페치
                name = "mealFoodsSubgraph",
                attributeNodes = @NamedAttributeNode("food") // MealFood 엔티티의 food 필드
            )
        }
    )
})
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_template_id", nullable = true)
    private DietTemplate dietTemplate;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_type", length = 20)
    private DietType dietType;

    @Column(length = 30)
    private String name;

    @Column(name = "diet_date")
    private LocalDate dietDate;

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

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

    @Builder
    public Diet(User user, DietTemplate dietTemplate, DietType dietType, String name, LocalDate dietDate) {
        this.user = user;
        this.dietTemplate = dietTemplate;
        this.dietType = dietType;
        this.name = name;
        this.dietDate = dietDate;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        meal.setDiet(this);
    }
}