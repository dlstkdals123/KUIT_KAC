package org.example.kuit_kac.domain.exercise.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "exercise_set")
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_exercise_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RoutineExercise routineExercise;

    @Column(nullable = true)
    private Integer count = 0;

    @Column(name = "weight_kg", nullable = true)
    private Integer weightKg = 0;

    @Column(name = "weight_num", nullable = true)
    private Integer weightNum = 0;

    @Column(nullable = true)
    private Integer distance = 0;

    @Column(nullable = true)
    private Double time = 0.0;

    @Column(name = "set_order", nullable = true)
    private Integer setOrder = 0;

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
