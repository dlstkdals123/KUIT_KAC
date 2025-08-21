package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "나만의 운동 루틴 수정 요청 DTO")
public record RoutineTemplateUpdateRequest(
    @Schema(description = "루틴 이름", example = "상체 루틴1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "루틴 이름은 필수입니다.")
    String name,
    
    @Schema(description = "루틴 운동 목록 (중복 불가, 1개 이상)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "루틴 운동 목록은 필수입니다.")
    @Size(min = 1, message = "루틴 운동 목록은 한 개 이상 등록해야 합니다.")
    List<@Valid RoutineExerciseCreateRequest> routineExercises
) {}
