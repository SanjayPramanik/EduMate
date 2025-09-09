package com.edumate.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AIGenerationRequest {

    @NotBlank(message = "Note content is required")
    private String noteContent;

    @NotNull(message = "Number of items is required")
    @Min(value = 1, message = "Number of items must be at least 1")
    private Integer numberOfItems;

    private String noteId;
    private String userId;

    public AIGenerationRequest() {}

    public AIGenerationRequest(String noteContent, Integer numberOfItems) {
        this.noteContent = noteContent;
        this.numberOfItems = numberOfItems;
    }

    // Getters and Setters
    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
