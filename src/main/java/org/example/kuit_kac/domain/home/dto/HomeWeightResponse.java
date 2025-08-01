package org.example.kuit_kac.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.home.repository.WeightRepository;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "체중 생성/수정 응답 DTO")
public class HomeWeightResponse {
    private double weight;
    private LocalDateTime createdAt;

    public static HomeWeightResponse from(Weight weight) {
        return new HomeWeightResponse(weight.getWeight(), weight.getCreatedAt());
    }
}
