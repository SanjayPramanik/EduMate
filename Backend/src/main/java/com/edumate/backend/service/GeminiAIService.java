package com.edumate.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@Service
public class GeminiAIService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final List<String> apiKeys;
    private final Random random;

    @Value("${ai.gemini.base-url}")
    private String baseUrl;

    public GeminiAIService() {
        this.apiKeys = List.of("AIzaSyAtRJvydFU-UZUDnK3Eq3Zx5WE_XKax8Z4");
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
    }

    public String generateSummary(String content) {
        String prompt = "Please provide a comprehensive summary of the following content. " +
                "Focus on the main points, key concepts, and important details. " +
                "Make it clear and well-structured:\n\n" + content;

        return callGeminiAPI(prompt);
    }

    public String generateMCQs(String content, int numberOfQuestions) {
        String prompt = "Based on the following content, generate " + numberOfQuestions + 
                " multiple choice questions with 4 options each. " +
                "Format the response as JSON with the following structure:\n" +
                "{\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"Question text here\",\n" +
                "      \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"],\n" +
                "      \"correctAnswer\": 0,\n" +
                "      \"explanation\": \"Explanation of the correct answer\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n\n" +
                "Content:\n" + content;

        return callGeminiAPI(prompt);
    }

    public String generateFlashcards(String content, int numberOfCards) {
        String prompt = "Based on the following content, generate " + numberOfCards + 
                " flashcards. Format the response as JSON with the following structure:\n" +
                "{\n" +
                "  \"flashcards\": [\n" +
                "    {\n" +
                "      \"front\": \"Question or term\",\n" +
                "      \"back\": \"Answer or definition\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n\n" +
                "Content:\n" + content;

        return callGeminiAPI(prompt);
    }

    private String callGeminiAPI(String prompt) {
        try {
            // Get a random API key for load balancing
            String apiKey = apiKeys.get(random.nextInt(apiKeys.size()));
            
            String requestBody = String.format(
                "{\n" +
                "  \"contents\": [{\n" +
                "    \"parts\": [{\n" +
                "      \"text\": \"%s\"\n" +
                "    }]\n" +
                "  }],\n" +
                "  \"generationConfig\": {\n" +
                "    \"temperature\": 0.7,\n" +
                "    \"topK\": 40,\n" +
                "    \"topP\": 0.95,\n" +
                "    \"maxOutputTokens\": 2048\n" +
                "  }\n" +
                "}",
                prompt.replace("\"", "\\\"").replace("\n", "\\n")
            );

            String response = webClient.post()
                    .uri(baseUrl + "?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return extractContentFromResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Gemini API: " + e.getMessage(), e);
        }
    }

    private String extractContentFromResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode candidates = rootNode.path("candidates");
            
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");
                
                if (parts.isArray() && parts.size() > 0) {
                    return parts.get(0).path("text").asText();
                }
            }
            
            throw new RuntimeException("Invalid response format from Gemini API");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini API response: " + e.getMessage(), e);
        }
    }
}


