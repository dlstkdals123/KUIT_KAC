package org.example.kuit_kac.domain.ai.controller;

import java.util.ArrayList;
import java.util.List;

import org.example.kuit_kac.config.GptConfig;
import org.example.kuit_kac.domain.ai.dto.AiGenerateResponse;
import org.example.kuit_kac.domain.ai.service.AiDietService;
import org.example.kuit_kac.domain.diet.dto.AiPlanGenerateRequest;
import org.example.kuit_kac.domain.diet.dto.DietAiPlanTotalCreateRequest;
import org.example.kuit_kac.domain.diet.dto.DietRecordProfileResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI 관리", description = "AI를 관리하는 API입니다.")
@RequiredArgsConstructor
public class AiController {

    private final GptConfig gptConfig;

    private final UserService userService;
    private final AiDietService aiDietService;
    private final DietService dietService;

    @PostMapping("/diets")
    @Operation(summary = "AI 식단 예측", description = "사용자 정보를 기반으로 AI가 개인화된 식단을 예측합니다.")
    public ResponseEntity<AiGenerateResponse> getResponse(
        @RequestBody @Valid AiPlanGenerateRequest request,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        User user = userService.getUserById(p.getUserId());
        String systemPrompt = aiDietService.getSystemPrompt(user, request);
        String userPrompt = aiDietService.getUserPrompt(request);
        String dayResponse = gptConfig.getResponse(systemPrompt, userPrompt);
        String response = aiDietService.convertDayToDate(dayResponse, request);
        AiGenerateResponse aiGenerateResponse = aiDietService.validateResponse(response);
        return ResponseEntity.ok(aiGenerateResponse);
    }

    @PostMapping("/diets/create")
    @Operation(summary = "AI 식단 생성", description = "사용자 정보를 기반으로 AI가 개인화된 식단을 생성합니다.")
    public ResponseEntity<List<DietRecordProfileResponse>> createDiet(
        @RequestBody @Valid DietAiPlanTotalCreateRequest createRequest,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        User user = userService.getUserById(p.getUserId());
        List<Diet> diets = new ArrayList<>();
        createRequest.plans().forEach(dietAiPlanDay -> {
            dietAiPlanDay.diets().forEach(dietAiPlan -> {
                Diet diet = dietService.createAiPlanDiet(user, dietAiPlan.dietType(), dietAiPlanDay.dietDate(), dietAiPlan.aiDietFoods());
                diets.add(diet);
            });
        });

        List<DietRecordProfileResponse> dietRecordProfileResponses = diets.stream()
                .map(DietRecordProfileResponse::from)
                .toList();

        return ResponseEntity.ok(dietRecordProfileResponses);
    }
}