package org.example.kuit_kac.domain.ai.controller;

import java.util.ArrayList;
import java.util.List;

import org.example.kuit_kac.config.GptConfig;
import org.example.kuit_kac.domain.ai.service.AiDietService;
import org.example.kuit_kac.domain.diet.dto.AiPlanGenerateRequest;
import org.example.kuit_kac.domain.diet.dto.DietAiPlanTotalCreateRequest;
import org.example.kuit_kac.domain.diet.dto.DietRecordProfileResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<String> getResponse(
        @RequestBody @Valid AiPlanGenerateRequest request,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            // throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        // User user = userService.getUserById(p.getUserId());
        User user = userService.getUserById(1L);
        String systemPrompt = aiDietService.getSystemPrompt(user, request);
        String userPrompt = aiDietService.getUserPrompt(request);
        String dayResponse = gptConfig.getResponse(systemPrompt, userPrompt);
        String response = aiDietService.convertDayToDate(dayResponse, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/diets/create")
    public ResponseEntity<List<DietRecordProfileResponse>> createDiet(
        @RequestBody @Valid DietAiPlanTotalCreateRequest createRequest,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            // throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        // User user = userService.getUserById(p.getUserId());
        User user = userService.getUserById(1L);
        List<Diet> diets = new ArrayList<>();
        createRequest.plans().forEach(dietAiPlanDay -> {
            dietAiPlanDay.plans().forEach(dietAiPlan -> {
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