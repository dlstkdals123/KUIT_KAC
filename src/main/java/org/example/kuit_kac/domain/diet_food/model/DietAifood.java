package org.example.kuit_kac.domain.diet_food.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;
import lombok.NoArgsConstructor;

import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.food.model.Aifood;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "diet_aifood")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "DietAifood.withAifood", attributeNodes = @NamedAttributeNode("aifood"))
})
public class DietAifood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Diet diet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aifood_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Aifood aifood;

    @Column(nullable = false)
    private double quantity;

    @Column(name = "diet_time", nullable = true)
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

    @Builder
    public DietAifood(Diet diet, Aifood aifood, double quantity) {
        this.diet = diet;
        this.aifood = aifood;
        this.quantity = quantity;
    }

    public void setAifood(Aifood aifood) {
        this.aifood = aifood;
    }
}