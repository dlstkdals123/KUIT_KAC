package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "exercise")
@Schema(description = "운동 종목")
public class Exercise {
    @Schema(description = "운동 ID")
    private Long id;

    @Schema(description = "운동 이름")
    private String name;

    @Schema(description = "타겟 근육 그룹")
    private String targetMuscleGroup;

    @Schema(description = "MET 값")
    private Double metValue;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;
}
