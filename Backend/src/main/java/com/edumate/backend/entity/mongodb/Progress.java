package com.edumate.backend.entity.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "progress")
public class Progress {

    @Id
    @Field("quiz_id")
    private String quizId;

    @Field("progress_id")
    private String progressId;

    @Field("user_id")
    private String userId;

    @Field("project_id")
    private String projectId;

    @Field("study_date")
    private LocalDate studyDate;

    @Field("flashcard_id")
    private String flashcardId;

    // Constructors
    public Progress() {}

    public Progress(String quizId, String progressId, String userId, String projectId, 
                    LocalDate studyDate, String flashcardId) {
        this.quizId = quizId;
        this.progressId = progressId;
        this.userId = userId;
        this.projectId = projectId;
        this.studyDate = studyDate;
        this.flashcardId = flashcardId;
    }

    // Getters and Setters
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
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

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }

    public String getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(String flashcardId) {
        this.flashcardId = flashcardId;
    }
}
