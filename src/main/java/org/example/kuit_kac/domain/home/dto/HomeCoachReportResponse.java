package org.example.kuit_kac.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kuit_kac.domain.home.model.Level;

@Getter
@AllArgsConstructor
@Schema(description = "햄코치 관찰일지에 포함된 상세 정보 DTO")
public class HomeCoachReportResponse {
    // 외식횟수, 공복시간 적음/많음 여부, 술자리 적음/많음 여부, 배달어플 빈도, 야식 적음/많음 여부

    @Schema(description = "외식 횟수", example = "1")
    private long diningOutNum; // 외식 횟수

    @Schema(description = "공복시간 정도")
    private Level fastingLevel; // 공복시간

    @Schema(description = "술자리 빈도")
    private Level dringkingLevel; // 술자리

    @Schema(description = "야식 빈도")
    private Level lateNightLevel; // 야식
}
