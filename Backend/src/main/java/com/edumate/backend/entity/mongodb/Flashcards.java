package com.edumate.backend.entity.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "flashcards")
public class Flashcards {

    @Id
    @Field("flashcard_id")
    private String flashcardId;

    @Field("note_id")
    private String noteId;

    @Field("user_id")
    private String userId;

    @Field("project_id")
    private String projectId;

    @Field("flashcards")
    private List<String> flashcards;

    // Constructors
    public Flashcards() {}

    public Flashcards(String noteId, String userId, String projectId, List<String> flashcards) {
        this.noteId = noteId;
        this.userId = userId;
        this.projectId = projectId;
        this.flashcards = flashcards;
    }

    // Getters and Setters
    public String getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(String flashcardId) {
        this.flashcardId = flashcardId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<String> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<String> flashcards) {
        this.flashcards = flashcards;
    }
}
