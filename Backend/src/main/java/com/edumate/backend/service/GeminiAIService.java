package com.edumate.backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAIService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAIService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    private final HttpClient httpClient;
    private final Gson gson;

    public GeminiAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.gson = new Gson();
    }

    /**
     * Generate quiz questions from note content
     */
    public String generateQuiz(String noteContent, int numberOfQuestions) throws IOException, InterruptedException {
        validateInput(noteContent, "Note content");
        if (numberOfQuestions <= 0) {
            throw new IllegalArgumentException("Number of questions must be greater than 0");
        }
        
        logger.info("Generating {} quiz questions from note content", numberOfQuestions);
        String prompt = createQuizPrompt(noteContent, numberOfQuestions);
        return callGeminiAPI(prompt);
    }

    /**
     * Generate flashcards from note content
     */
    public String generateFlashcards(String noteContent, int numberOfFlashcards) throws IOException, InterruptedException {
        validateInput(noteContent, "Note content");
        if (numberOfFlashcards <= 0) {
            throw new IllegalArgumentException("Number of flashcards must be greater than 0");
        }
        
        logger.info("Generating {} flashcards from note content", numberOfFlashcards);
        String prompt = createFlashcardPrompt(noteContent, numberOfFlashcards);
        return callGeminiAPI(prompt);
    }

    /**
     * Generate summary from note content
     */
    public String generateSummary(String noteContent) throws IOException, InterruptedException {
        validateInput(noteContent, "Note content");
        
        logger.info("Generating summary from note content");
        String prompt = createSummaryPrompt(noteContent);
        return callGeminiAPI(prompt);
    }

    /**
     * Validate input parameters
     */
    private void validateInput(String input, String fieldName) {
        if (!StringUtils.hasText(input)) {
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
            "{\"question\": \"question text\", \"options\": [\"A\", \"B\", \"C\", \"D\"], \"correctAnswer\": \"A\", \"explanation\": \"explanation text\"}. " +
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

    private String callGeminiAPI(String prompt) throws IOException, InterruptedException {
        // Create request body with proper structure
        JsonObject requestBody = new JsonObject();
        
        // Create parts array with text content
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);
        
        // Create contents object
        JsonObject content = new JsonObject();
        content.add("parts", gson.toJsonTree(Arrays.asList(textPart)));
        
        // Add contents array to request body
        requestBody.add("contents", gson.toJsonTree(Arrays.asList(content)));

        // Add generation config for better responses
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("topK", 40);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 2048);
        requestBody.add("generationConfig", generationConfig);

        // Validate API key
        if (!StringUtils.hasText(apiKey) || apiKey.equals("YOUR_GEMINI_API_KEY_HERE")) {
            throw new IllegalStateException("Gemini API key is not configured. Please set gemini.api.key in application.properties");
        }

        String requestBodyJson = gson.toJson(requestBody);
        logger.debug("Sending request to Gemini API: {}", apiUrl);
        logger.trace("Request body: {}", requestBodyJson);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .timeout(Duration.ofMinutes(2))
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        logger.debug("Received response with status: {}", response.statusCode());
        logger.trace("Response body: {}", response.body());

        if (response.statusCode() != 200) {
            String errorMessage = "Gemini API request failed with status: " + response.statusCode();
            if (response.body() != null && !response.body().isEmpty()) {
                errorMessage += ", body: " + response.body();
            }
            logger.error("API call failed: {}", errorMessage);
            throw new IOException(errorMessage);
        }

        // Parse response
        try {
            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            
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
                throw new IOException("Gemini API error: " + errorMessage);
            }
            
            throw new IOException("Unexpected response format from Gemini API: " + response.body());
            
        } catch (JsonSyntaxException e) {
            logger.error("Failed to parse JSON response from Gemini API", e);
            throw new IOException("Invalid JSON response from Gemini API: " + e.getMessage(), e);
        }
    }
}
