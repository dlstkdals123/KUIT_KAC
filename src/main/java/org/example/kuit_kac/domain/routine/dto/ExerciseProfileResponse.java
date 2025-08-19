package org.example.kuit_kac.domain.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import org.example.kuit_kac.domain.routine.model.Exercise;

@Getter
@AllArgsConstructor
@Schema(description = "운동의 정보를 담는 응답 DTO입니다. 운동 검색에 사용됩니다.")
public class ExerciseProfileResponse {
    @Schema(description = "운동의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "운동 이름", example = "벤치프레스")
    private String name;

    @Schema(description = "운동 대상 근육 (가능한 값: 복근, 외전근, 내전근, 유산소, 무산소, 등, 이두근, 종아리, 가슴, 전완근, 둔근, 햄스트링, 고관절 굴곡근, 대퇴사두근, 정강이, 어깨, 승모근, 삼두근)", example = "가슴", requiredMode = Schema.RequiredMode.REQUIRED)
    private String targetMuscleType;

    @Schema(description = "운동 MET 값 (단위: MET)", example = "5.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double metValue;

    @Schema(description = "운동 정보 생성일시 (형식: YYYY-MM-DDTHH:MM:SS)", example = "2025-07-24T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "운동 정보 최종 수정일시 (형식: YYYY-MM-DDTHH:MM:SS)", example = "2025-07-24T12:00:00")
    private LocalDateTime updatedAt;

    public static ExerciseProfileResponse from(Exercise exercise) {
        return new ExerciseProfileResponse(
            exercise.getId(), 
            exercise.getName(), 
            exercise.getTargetMuscleType().getKoreanName(), 
            exercise.getMetValue(), 
            exercise.getCreatedAt(), 
            exercise.getUpdatedAt());
    }
}
