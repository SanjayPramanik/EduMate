package com.edumate.backend.entity.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "quizzes")
public class Quiz {

    @Id
    @Field("quiz_id")
    private String quizId;

    @Field("questions")
    private String questions;

    @Field("solutions")
    private String solutions;

    @Field("iscorrect")
    private Boolean isCorrect;

    @Field("timetaken")
    private Integer timeTaken;

    @Field("attempt_date")
    private LocalDate attemptDate;

    @Field("attempt_time")
    private LocalTime attemptTime;

    // Constructors
    public Quiz() {}

    public Quiz(String questions, String solutions, Boolean isCorrect, Integer timeTaken, 
                LocalDate attemptDate, LocalTime attemptTime) {
        this.questions = questions;
        this.solutions = solutions;
        this.isCorrect = isCorrect;
        this.timeTaken = timeTaken;
        this.attemptDate = attemptDate;
        this.attemptTime = attemptTime;
    }

    // Getters and Setters
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    public LocalDate getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDate attemptDate) {
        this.attemptDate = attemptDate;
    }

    public LocalTime getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(LocalTime attemptTime) {
        this.attemptTime = attemptTime;
    }
}
