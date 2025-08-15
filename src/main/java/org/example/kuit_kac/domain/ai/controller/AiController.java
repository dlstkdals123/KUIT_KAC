package org.example.kuit_kac.domain.ai.controller;

import org.example.kuit_kac.config.GptConfig;
import org.example.kuit_kac.domain.ai.service.AiDietService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI 관리", description = "AI를 관리하는 API입니다.")
@RequiredArgsConstructor
public class AiController {

    private final GptConfig gptConfig;

    private final AiDietService aiDietService;

    @GetMapping("/diets")
    public ResponseEntity<String> getResponse() {
        String systemPrompt = aiDietService.getSystemPrompt();
        String userPrompt = aiDietService.getUserPrompt();
        return gptConfig.getResponse(systemPrompt, userPrompt);
    }
}