package org.example.kuit_kac.domain.meal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.meal.model.Meal;
import org.example.kuit_kac.domain.meal.model.MealType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "끼니 정보를 담는 응답 DTO입니다. 끼니 조회, 등록 등의 API 응답에 사용됩니다.")
public class MealResponse {
    @Schema(description = "끼니의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "연결된 사용자의 고유 식별자 (User ID)", example = "101")
    private Long userId;

    @Schema(description = "연결된 식단의 고유 식별자 (Diet ID)", example = "101")
    private Long dietId;

    @Schema(description = "끼니 이름", example = "아침 식단")
    private String name;

    @Schema(description = "끼니 유형", example = "LUNCH", allowableValues = {"BREAKFAST", "LUNCH", "DINNER", "SNACK", "TEMPLATE"})
    private MealType mealType;

    @Schema(description = "끼니를 섭취한 시간", example = "2025-07-10T12:30:00")
    private LocalDateTime mealTime;

    @Schema(description = "끼니 정보 생성일시", example = "2025-07-10T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "끼니 정보 최종 수정일시", example = "2025-07-10T12:00:00")
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

    public static MealResponse from(Meal meal) {
        return new MealResponse(
                meal.getId(),
                meal.getUser().getId(),
                (meal.getDiet() != null) ? meal.getDiet().getId() : null,
                meal.getName(),
                meal.getMealType(),
                meal.getMealTime(),
                meal.getCreatedAt(),
                meal.getUpdatedAt()
        );
    }
}