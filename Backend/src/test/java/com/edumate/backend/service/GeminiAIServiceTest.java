package com.edumate.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "gemini.api.key=AIzaSyAtRJvydFU-UZUDnK3Eq3Zx5WE_XKax8Z4",
    "gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"
})
public class GeminiAIServiceTest {

    private GeminiAIService geminiAIService;
    private GeminiAIServiceSimple geminiAIServiceSimple;

    @BeforeEach
    void setUp() {
        geminiAIService = new GeminiAIService();
        geminiAIServiceSimple = new GeminiAIServiceSimple();
    }

    @Test
    void testValidateInput_ValidInput_ShouldPass() {
        String validContent = "This is a valid note content with more than 10 characters.";
        
        // Should not throw any exception
        assertDoesNotThrow(() -> {
            geminiAIServiceSimple.generateSummary(validContent);
        });
    }

    @Test
    void testValidateInput_NullInput_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateSummary(null);
        });
    }

    @Test
    void testValidateInput_EmptyInput_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateSummary("");
        });
    }

    @Test
    void testValidateInput_TooShortInput_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateSummary("short");
        });
    }

    @Test
    void testGenerateQuiz_InvalidNumberOfQuestions_ShouldThrowException() {
        String validContent = "This is valid content for testing quiz generation.";
        
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateQuiz(validContent, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateQuiz(validContent, -1);
        });
    }

    @Test
    void testGenerateFlashcards_InvalidNumberOfFlashcards_ShouldThrowException() {
        String validContent = "This is valid content for testing flashcard generation.";
        
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateFlashcards(validContent, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            geminiAIServiceSimple.generateFlashcards(validContent, -1);
        });
    }

    // Note: Integration tests with actual API calls should be in separate test class
    // to avoid making actual API calls during unit testing
}
