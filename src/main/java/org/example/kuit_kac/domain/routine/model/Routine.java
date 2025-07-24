package org.example.kuit_kac.domain.routine.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.kuit_kac.domain.user.model.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "routine")
@NamedEntityGraphs({
    // Routine.withRoutineExercisesAndExercise: routineExercises -> exercise 를 페치
    @NamedEntityGraph(
        name = "Routine.withRoutineExercisesAndExercise",
        attributeNodes = @NamedAttributeNode(
            value = "routineExercises",
            subgraph = "routineExercisesSubgraph"
        ),
        subgraphs = @NamedSubgraph(
            name = "routineExercisesSubgraph",
            attributeNodes = @NamedAttributeNode("exercise")
        )
    ),
    // Routine.withRoutineExercises: routineExercises 필드를 페치
    @NamedEntityGraph(
        name = "Routine.withRoutineExercises",
        attributeNodes = @NamedAttributeNode("routineExercises")
    )
})

public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoutineType routineType;

    @Column(nullable = true)
    private LocalDateTime routineTime;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineExercise> routineExercises = new ArrayList<>();

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

    public Routine(User user, String name, LocalDateTime routineTime, RoutineType routineType) {
        this.user = user;
        this.name = name;
        this.routineTime = routineTime;
        this.routineType = routineType;
    }

    public void addRoutineExercise(RoutineExercise routineExercise) {
        this.routineExercises.add(routineExercise);
        routineExercise.setRoutine(this);
    }
}
