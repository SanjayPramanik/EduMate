package com.edumate.backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alternative implementation using RestTemplate for better compatibility
 */
@Service("geminiAIServiceSimple")
public class GeminiAIServiceSimple {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAIServiceSimple.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final Gson gson;

    public GeminiAIServiceSimple() {
        this.restTemplate = new RestTemplate();
        this.gson = new Gson();
    }

    public String generateQuiz(String noteContent, int numberOfQuestions) {
        validateInput(noteContent, "Note content");
        if (numberOfQuestions <= 0) {
            throw new IllegalArgumentException("Number of questions must be greater than 0");
        }

        logger.info("Generating {} quiz questions from note content", numberOfQuestions);
        String prompt = createQuizPrompt(noteContent, numberOfQuestions);
        return callGeminiAPI(prompt);
    }

    public String generateFlashcards(String noteContent, int numberOfFlashcards) {
        validateInput(noteContent, "Note content");
        if (numberOfFlashcards <= 0) {
            throw new IllegalArgumentException("Number of flashcards must be greater than 0");
        }

        logger.info("Generating {} flashcards from note content", numberOfFlashcards);
        String prompt = createFlashcardPrompt(noteContent, numberOfFlashcards);
        return callGeminiAPI(prompt);
    }

    public String generateSummary(String noteContent) {
        validateInput(noteContent, "Note content");

        logger.info("Generating summary from note content");
        String prompt = createSummaryPrompt(noteContent);
        return callGeminiAPI(prompt);
    }

    private void validateInput(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        if (input.trim().length() < 10) {
            throw new IllegalArgumentException(fieldName + " must be at least 10 characters long");
        }
    }

    private String createQuizPrompt(String noteContent, int numberOfQuestions) {
        return String.format(
            "Based on the following educational content, generate %d multiple-choice questions with 4 options each. " +
            "Format the response as a JSON array where each question has the structure: " +
            "{\"question\": \"question text\", \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"], \"correctAnswer\": \"A\", \"explanation\": \"explanation text\"}. " +
            "Make sure the questions are comprehensive and test understanding of key concepts.\n\nContent:\n%s",
            numberOfQuestions, noteContent
        );
    }

    private String createFlashcardPrompt(String noteContent, int numberOfFlashcards) {
        return String.format(
            "Based on the following educational content, generate %d flashcards for effective studying. " +
            "Format the response as a JSON array where each flashcard has the structure: " +
            "{\"front\": \"question or term\", \"back\": \"answer or definition\", \"category\": \"topic category\"}. " +
            "Focus on key terms, concepts, and important facts that students should memorize.\n\nContent:\n%s",
            numberOfFlashcards, noteContent
        );
    }

    private String createSummaryPrompt(String noteContent) {
        return String.format(
            "Create a comprehensive summary of the following educational content. " +
            "The summary should capture all key points, main concepts, and important details in a well-organized format. " +
            "Use bullet points and clear structure to make it easy to study from.\n\nContent:\n%s",
            noteContent
        );
    }

    private String callGeminiAPI(String prompt) {
        // Validate API key
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("YOUR_GEMINI_API_KEY_HERE")) {
            throw new IllegalStateException("Gemini API key is not configured. Please set gemini.api.key in application.properties");
        }

        try {
            // Create request body
            Map<String, Object> requestBody = createRequestBody(prompt);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Make the request
            String url = apiUrl + "?key=" + apiKey;
            logger.debug("Sending request to Gemini API: {}", url);

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );

            logger.debug("Received response with status: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseResponse(response.getBody());
            } else {
                throw new RuntimeException("Failed to get response from Gemini API");
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error calling Gemini API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Gemini API error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling Gemini API", e);
            throw new RuntimeException("Failed to generate content: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // Create parts
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        
        // Create content
        Map<String, Object> content = new HashMap<>();
        content.put("parts", Arrays.asList(part));
        
        // Add contents
        requestBody.put("contents", Arrays.asList(content));
        
        // Add generation config
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("topK", 40);
        generationConfig.put("topP", 0.95);
        generationConfig.put("maxOutputTokens", 2048);
        requestBody.put("generationConfig", generationConfig);
        
        return requestBody;
    }

    private String parseResponse(String responseBody) {
        try {
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
            
            if (responseJson.has("candidates") && responseJson.getAsJsonArray("candidates").size() > 0) {
                JsonObject candidate = responseJson.getAsJsonArray("candidates").get(0).getAsJsonObject();
                if (candidate.has("content") && candidate.getAsJsonObject("content").has("parts")) {
                    JsonObject partsResponse = candidate.getAsJsonObject("content")
                        .getAsJsonArray("parts").get(0).getAsJsonObject();
                    String generatedText = partsResponse.get("text").getAsString();
                    logger.info("Successfully generated AI content with {} characters", generatedText.length());
                    return generatedText;
                }
            }
            
            // Handle error responses
            if (responseJson.has("error")) {
                JsonObject error = responseJson.getAsJsonObject("error");
                String errorMessage = error.has("message") ? error.get("message").getAsString() : "Unknown API error";
                logger.error("Gemini API returned error: {}", errorMessage);
                throw new RuntimeException("Gemini API error: " + errorMessage);
            }
            
            throw new RuntimeException("Unexpected response format from Gemini API");
            
        } catch (Exception e) {
            logger.error("Failed to parse response from Gemini API", e);
            throw new RuntimeException("Failed to parse API response: " + e.getMessage(), e);
        }
    }
}
