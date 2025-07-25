package org.example.kuit_kac.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import com.theokanning.openai.service.OpenAiService;

@Configuration
public class GptConfig {

    public final static String BASE_URL = "https://api.openai.com/v1/chat/completions";
    public final static String MODEL = "gpt-4o-mini";
    public final static double TEMPERATURE = 1.0;
    public final static double TOP_P = 1.0;
    public final static int MAX_TOKENS = 1000;
    public final static Duration TIMEOUT = Duration.ofSeconds(30);
    public final static String SYSTEM_PROMPT = "You are a helpful diet assistant that provides personalized and healthy diet schedules.";

    @Value("${openai.api-key}")
    private String apiKey;
    
    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey, TIMEOUT);
    }

    public ResponseEntity<String> getResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        JSONObject requestBody = new JSONObject();
        List<Map<String, Object>> messages = new ArrayList<>();

        // system 메시지 추가
        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);

        // user 메시지 추가
        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);

        JSONArray messagesArray = new JSONArray(messages);

        try {
            requestBody.put("messages", messagesArray);
            requestBody.put("temperature", TEMPERATURE);
            requestBody.put("top_p", TOP_P);
            requestBody.put("max_tokens", MAX_TOKENS);
            requestBody.put("model", MODEL);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, requestEntity, String.class);
        return response;
    }
}
