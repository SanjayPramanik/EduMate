package com.edumate.backend.entity.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "mcqs")
public class MCQ {

    @Id
    @Field("mcq_id")
    private String mcqId;

    @Field("user_id")
    private String userId;

    @Field("project_id")
    private String projectId;

    @Field("questions")
    private List<String> questions;

    @Field("note_id")
    private String noteId;

    // Constructors
    public MCQ() {}

    public MCQ(String userId, String projectId, List<String> questions, String noteId) {
        this.userId = userId;
        this.projectId = projectId;
        this.questions = questions;
        this.noteId = noteId;
    }

    // Getters and Setters
    public String getMcqId() {
        return mcqId;
    }

    public void setMcqId(String mcqId) {
        this.mcqId = mcqId;
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

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
