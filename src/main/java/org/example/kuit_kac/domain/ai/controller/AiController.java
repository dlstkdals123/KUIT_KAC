package org.example.kuit_kac.domain.ai.controller;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.example.kuit_kac.config.GptConfig;
import org.example.kuit_kac.domain.ai.dto.AiGenerateResponse;
import org.example.kuit_kac.domain.ai.dto.AiPlanGenerateRequest;
import org.example.kuit_kac.domain.ai.dto.DietAiFoodCreateRequest;
import org.example.kuit_kac.domain.ai.dto.DietAiPlanCreateRequest;
import org.example.kuit_kac.domain.ai.dto.DietAiPlanTotalCreateRequestAndResponse;
import org.example.kuit_kac.domain.ai.service.AiService;
import org.example.kuit_kac.domain.diet.dto.DietRecordProfileResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.diet_food.dto.DietFoodCreateRequest;
import org.example.kuit_kac.domain.food.dto.FoodProfileResponse;
import org.example.kuit_kac.domain.food.model.Food;
import org.example.kuit_kac.domain.food.service.FoodService;
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
    private final AiService aiService;
    private final DietService dietService;
    private final FoodService foodService;

    @PostMapping("/diets")
    @Operation(summary = "AI 식단 생성", description = "사용자 정보를 기반으로 AI가 개인화된 식단을 생성합니다.")
    public ResponseEntity<DietAiPlanTotalCreateRequestAndResponse> getResponse(
        @RequestBody @Valid AiPlanGenerateRequest request,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        User user = userService.getUserById(p.getUserId());
        String systemPrompt = aiService.getSystemPrompt(user, request);
        String userPrompt = aiService.getUserPrompt(request);
        String dayResponse = gptConfig.getResponse(systemPrompt, userPrompt);
        String response = aiService.convertDayToDate(dayResponse, request);
        AiGenerateResponse aiGenerateResponse = aiService.validateResponse(response);
        List<DietAiPlanCreateRequest> dietPlanCreateRequests = aiGenerateResponse.response().entrySet().stream()
                .map(dateEntry -> {
                    LocalDate date = LocalDate.parse(dateEntry.getKey());
                    return dateEntry.getValue().entrySet().stream()
                            .map(typeEntry -> {
                                String dietType = typeEntry.getKey();
                                List<DietAiFoodCreateRequest> dietAiFoodCreateRequests = typeEntry.getValue().stream()
                                        .map(foodItem -> {
                                            Food food = foodService.createFood(foodItem);
                                            FoodProfileResponse foodProfileResponse = FoodProfileResponse.from(food);
                                            return DietAiFoodCreateRequest.from(1.0, foodProfileResponse);
                                        })
                                        .collect(Collectors.toList());
                                return DietAiPlanCreateRequest.from(user, dietType, date, dietAiFoodCreateRequests);
                            })
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
        DietAiPlanTotalCreateRequestAndResponse dietAiPlanTotalCreateResponse = DietAiPlanTotalCreateRequestAndResponse.from(request, dietPlanCreateRequests);
        return ResponseEntity.ok(dietAiPlanTotalCreateResponse);
    }

    @PostMapping("/diets/create")
    @Operation(summary = "AI 식단 추가", description = "사용자 정보를 기반으로 AI가 개인화된 식단을 추가합니다.")
    public ResponseEntity<List<DietRecordProfileResponse>> createDiet(
        @RequestBody @Valid DietAiPlanTotalCreateRequestAndResponse createRequest,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        User user = userService.getUserById(p.getUserId());

        List<Diet> diets = new ArrayList<>();

        createRequest.dietAiPlanGenerateRequest().dietActivities().forEach(dietActivity -> {
            Diet diet = dietService.createActivityDiet(user, dietActivity.dietType(), dietActivity.dietDate(), dietActivity.dietEntryType());
            diets.add(diet);
        });
        
        createRequest.plans().forEach(dietPlan -> {
            List<DietFoodCreateRequest> dietFoods = dietPlan.foods().stream()
                    .map(foods -> DietFoodCreateRequest.from(foods.food().getId(), foods.quantity()))
                    .collect(Collectors.toList());
            
            Diet diet = dietService.createAiPlanDiet(user, dietPlan.dietType(), dietPlan.date(), dietFoods);
            diets.add(diet);
        });

        List<DietRecordProfileResponse> dietRecordProfileResponses = diets.stream()
                .map(DietRecordProfileResponse::from)
                .toList();

        return ResponseEntity.ok(dietRecordProfileResponses);
    }
}