package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "exercise_set")
@Schema(description = "운동 세트")
public class ExerciseSet {
    @Schema(description = "세트 ID")
    private Long id;

    @Schema(description = "루틴-운동 ID")
    private Long routineExerciseId;

    @Schema(description = "횟수")
    private Integer count;

    @Schema(description = "무게(kg)")
    private Integer weightKg;

    @Schema(description = "무게 개수")
    private Integer weightNum;

    @Schema(description = "거리")
    private Integer distance;

    @Schema(description = "시간")
    private Double time;

    @Schema(description = "세트 순서")
    private Integer setOrder;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;
}
