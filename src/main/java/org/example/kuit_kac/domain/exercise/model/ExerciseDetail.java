package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "exercise_detail")
@Schema(description = "운동 상세")
public class ExerciseDetail {
    @Schema(description = "운동 상세 ID")
    private Long id;

    @Schema(description = "루틴-운동 ID")
    private Long routineExerciseId;

    @Schema(description = "운동 시간(분)")
    private Integer time;

    @Schema(description = "운동 강도")
    private Intensity intensity;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;
}
