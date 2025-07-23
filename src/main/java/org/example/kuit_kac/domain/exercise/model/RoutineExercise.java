package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "routine_exercise")
@Schema(description = "루틴별 운동")
public class RoutineExercise {
    @Schema(description = "루틴-운동 ID")
    private Long id;

    @Schema(description = "루틴 ID")
    private Long routineId;

    @Schema(description = "운동 ID")
    private Long exerciseId;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;
}
