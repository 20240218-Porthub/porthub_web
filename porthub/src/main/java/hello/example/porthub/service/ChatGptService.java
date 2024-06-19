package hello.example.porthub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class ChatGptService {

    @Value("${chatGpt.key}")
    private String key;

    private static final String SYSTEM_PROMPT = "너는 다양한 분야의 멘토링, 구인구직, 개인 경험 포트폴리오에 대한 질문에 답하는 챗봇이야. 사용자가 입력한 질문에 대해 최대한 정확하고 유용한 답변만을 제공해야해. 또한 답변은 한 문장이고 반드시 40자 내외로 답해야해";

    public String sendGpt(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = UriComponentsBuilder
                    .fromUriString("https://api.openai.com/v1/chat/completions")
                    .build()
                    .encode()
                    .toUri();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + key);
            httpHeaders.add("Content-Type", "application/json");

            List<Message> messages = new ArrayList<>();
            messages.add(new Message("system", SYSTEM_PROMPT));
            messages.add(new Message("user", userMessage));

            Body body = new Body("gpt-4o", messages);

            HttpEntity<Body> requestEntity = new HttpEntity<>(body, httpHeaders);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
                    String.class);

            // 응답에서 메시지(content)만 추출
            JSONObject responseJson = new JSONObject(responseEntity.getBody());
            JSONArray choices = responseJson.getJSONArray("choices");
            String gptMessage = choices.getJSONObject(0).getJSONObject("message").getString("content");

            return gptMessage;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in ChatGptService.sendGpt: " + e.getMessage());
            return null;
        }
    }

    @AllArgsConstructor
    @Data
    static class Body {
        String model;
        List<Message> messages;
    }

    @AllArgsConstructor
    @Data
    static class Message {
        String role;
        String content;
    }
}
