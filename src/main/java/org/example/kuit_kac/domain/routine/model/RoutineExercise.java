package org.example.kuit_kac.domain.routine.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "routine_exercise")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "RoutineExercise.withExercise", 
        attributeNodes = @NamedAttributeNode("exercise")
    )
})
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Exercise exercise;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_detail_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RoutineDetail routineDetail;

    @Setter
    @OneToMany(mappedBy = "routineExercise", fetch = FetchType.LAZY)
    private List<RoutineSet> routineSets;

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

    public RoutineExercise(Routine routine, Exercise exercise) {
        this.routine = routine;
        this.exercise = exercise;
    }

    public void setRoutineSets(List<RoutineSet> routineSets) {
        this.routineSets.clear();
        this.routineSets = routineSets;
        routineSets.forEach(routineSet -> routineSet.setRoutineExercise(this));
    }
}
