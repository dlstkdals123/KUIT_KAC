package org.example.kuit_kac.domain.ai.controller;

import java.nio.file.attribute.UserPrincipal;

import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.config.GptConfig;
import org.example.kuit_kac.domain.ai.dto.AIPlanCreateRequest;
import org.example.kuit_kac.domain.ai.service.AiDietService;
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

    private final AiDietService aiDietService;

    @PostMapping("/diets")
    public ResponseEntity<String> getResponse(
        @RequestBody @Valid AIPlanCreateRequest request,
        @AuthenticationPrincipal UserPrincipal p
    ) {
        if (p == null) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        String systemPrompt = aiDietService.getSystemPrompt(request);
        String userPrompt = aiDietService.getUserPrompt(request);
        String dayResponse = gptConfig.getResponse(systemPrompt, userPrompt);
        String response = aiDietService.convertDayToDate(dayResponse, request);
        return ResponseEntity.ok(response);
    }
}