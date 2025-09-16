package com.edumate.backend.controller;

import com.edumate.backend.dto.AIGenerationRequest;
import com.edumate.backend.dto.AIGenerationResponse;
import com.edumate.backend.service.GeminiAIService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AIGenerationController {

    private static final Logger logger = LoggerFactory.getLogger(AIGenerationController.class);

    private final GeminiAIService geminiAIService;

    public AIGenerationController(GeminiAIService geminiAIService) {
        this.geminiAIService = geminiAIService;
    }

    /**
     * Generate quiz questions from note content
     */
    @PostMapping("/generate-quiz")
    public ResponseEntity<AIGenerationResponse> generateQuiz(@Valid @RequestBody AIGenerationRequest request) {
        try {
            logger.info("Generating quiz for note content with {} questions", request.getNumberOfItems());
            
            String quizContent = geminiAIService.generateQuiz(
                request.getNoteContent(), 
                request.getNumberOfItems()
            );
            
            AIGenerationResponse response = AIGenerationResponse.success(quizContent, "quiz");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating quiz: {}", e.getMessage(), e);
            AIGenerationResponse response = AIGenerationResponse.error(
                "Failed to generate quiz: " + e.getMessage(), 
                "quiz"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate flashcards from note content
     */
    @PostMapping("/generate-flashcards")
    public ResponseEntity<AIGenerationResponse> generateFlashcards(@Valid @RequestBody AIGenerationRequest request) {
        try {
            logger.info("Generating flashcards for note content with {} flashcards", request.getNumberOfItems());
            
            String flashcardsContent = geminiAIService.generateFlashcards(
                request.getNoteContent(), 
                request.getNumberOfItems()
            );
            
            AIGenerationResponse response = AIGenerationResponse.success(flashcardsContent, "flashcards");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating flashcards: {}", e.getMessage(), e);
            AIGenerationResponse response = AIGenerationResponse.error(
                "Failed to generate flashcards: " + e.getMessage(), 
                "flashcards"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate summary from note content
     */
    @PostMapping("/generate-summary")
    public ResponseEntity<AIGenerationResponse> generateSummary(@RequestBody AIGenerationRequest request) {
        try {
            logger.info("Generating summary for note content");
            
            // For summary, we don't need numberOfItems, so we can use the note content directly
            String summaryContent = geminiAIService.generateSummary(request.getNoteContent());
            
            AIGenerationResponse response = AIGenerationResponse.success(summaryContent, "summary");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating summary: {}", e.getMessage(), e);
            AIGenerationResponse response = AIGenerationResponse.error(
                "Failed to generate summary: " + e.getMessage(), 
                "summary"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate all types of content (quiz, flashcards, and summary) from note content
     */
    @PostMapping("/generate-all")
    public ResponseEntity<?> generateAllContent(@Valid @RequestBody AIGenerationRequest request) {
        try {
            logger.info("Generating all content types for note");
            
            // Generate all content types concurrently (you could use CompletableFuture for parallel execution)
            String quizContent = geminiAIService.generateQuiz(request.getNoteContent(), request.getNumberOfItems());
            String flashcardsContent = geminiAIService.generateFlashcards(request.getNoteContent(), request.getNumberOfItems());
            String summaryContent = geminiAIService.generateSummary(request.getNoteContent());
            
            // Create response object with all content
            var allContentResponse = new Object() {
                public final AIGenerationResponse quiz = AIGenerationResponse.success(quizContent, "quiz");
                public final AIGenerationResponse flashcards = AIGenerationResponse.success(flashcardsContent, "flashcards");
                public final AIGenerationResponse summary = AIGenerationResponse.success(summaryContent, "summary");
                public final boolean success = true;
            };
            
            return ResponseEntity.ok(allContentResponse);
            
        } catch (Exception e) {
            logger.error("Error generating all content: {}", e.getMessage(), e);
            
            var errorResponse = new Object() {
                public final boolean success = false;
                public final String errorMessage = "Failed to generate content: " + e.getMessage();
            };
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
