package org.example.kuit_kac.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "홈화면 DTO입니다. 일일섭취칼로리, 현재 체중 정보를 포함합니다.")
public class HomeSummaryRequest {
    private LocalDate date;
    private long userId;
}
