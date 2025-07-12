package org.example.kuit_kac.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietNameResponse;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자의 식단 이름 목록을 포함하는 응답 DTO입니다.")
public class UserDietResponse {

    @Schema(description = "사용자의 고유 식별자 (ID)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "사용자가 생성한 식단 이름 목록")
    private List<DietNameResponse> dietNames;
}