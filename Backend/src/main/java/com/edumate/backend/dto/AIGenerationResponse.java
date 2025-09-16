package com.edumate.backend.dto;

public class AIGenerationResponse {

    private String content;
    private String type; // "quiz", "flashcards", "summary"
    private boolean success;
    private String errorMessage;

    public AIGenerationResponse() {}

    public AIGenerationResponse(String content, String type, boolean success) {
        this.content = content;
        this.type = type;
        this.success = success;
    }

    public static AIGenerationResponse success(String content, String type) {
        return new AIGenerationResponse(content, type, true);
    }

    public static AIGenerationResponse error(String errorMessage, String type) {
        AIGenerationResponse response = new AIGenerationResponse();
        response.setErrorMessage(errorMessage);
        response.setType(type);
        response.setSuccess(false);
        return response;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
