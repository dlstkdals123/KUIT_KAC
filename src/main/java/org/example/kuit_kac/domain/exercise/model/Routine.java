package org.example.kuit_kac.domain.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "routine")
@Schema(description = "운동 루틴")
public class Routine {
    @Schema(description = "루틴 ID")
    private Long id;

    @Schema(description = "유저 ID")
    private Long userId;

    @Schema(description = "루틴 이름")
    private String name;

    @Schema(description = "운동 날짜")
    private LocalDateTime exerciseDate;

    @Schema(description = "루틴 타입")
    private RoutineType type;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;
}
