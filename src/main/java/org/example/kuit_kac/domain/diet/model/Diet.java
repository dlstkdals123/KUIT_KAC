package org.example.kuit_kac.domain.diet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.model.DietAifood;
import org.example.kuit_kac.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "diet")
@NamedEntityGraphs({
    // Diet.withDietFoods: dietFoods 필드를 페치
    @NamedEntityGraph(
        name = "Diet.withDietFoods",
        attributeNodes = @NamedAttributeNode("dietFoods")
    ),
    // Diet.withDietFoodsAndFood: dietFoods -> food 를 페치
    @NamedEntityGraph(
        name = "Diet.withDietFoodsAndFood",
        attributeNodes = @NamedAttributeNode(
            value = "dietFoods",
            subgraph = "dietFoodsSubgraph"
        ),
        subgraphs = @NamedSubgraph(
            name = "dietFoodsSubgraph",
            attributeNodes = @NamedAttributeNode("food")
        )
    ),
    // Diet.withDietAifoods: dietAifoods 필드를 페치
    @NamedEntityGraph(
        name = "Diet.withDietAifoods",
        attributeNodes = @NamedAttributeNode("dietAifoods")
    ),
    // Diet.withDietAifoodsAndAifood: dietAifoods -> aifood 를 페치
    @NamedEntityGraph(
        name = "Diet.withDietAifoodsAndAifood",
        attributeNodes = @NamedAttributeNode(
            value = "dietAifoods",
            subgraph = "dietAifoodsSubgraph"
        ),
        subgraphs = @NamedSubgraph(
            name = "dietAifoodsSubgraph",
            attributeNodes = @NamedAttributeNode("aifood")
        )
    )
})
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_type", nullable = false)
    private DietType dietType;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_entry_type")
    private DietEntryType dietEntryType;

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietFood> dietFoods = new ArrayList<>();

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietAifood> dietAifoods = new ArrayList<>();

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
    public Diet(User user, DietType dietType) {
        this.user = user;
        this.dietType = dietType;
    }

    public void addDietFood(DietFood dietFood) {
        this.dietFoods.add(dietFood);
        dietFood.setDiet(this);
    }

    public void addDietAifood(DietAifood dietAifood) {
        this.dietAifoods.add(dietAifood);
        dietAifood.setDiet(this);
    }
}