package com.edumate.backend.entity.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "flashcard")
public class Flashcard {

    @Id
    @Field("flashcard_id")
    private String flashcardId;

    @Field("content")
    private String content;

    @Field("timetaken")
    private Integer timeTaken;

    @Field("date")
    private LocalDate date;

    @Field("time")
    private LocalTime time;

    // Constructors
    public Flashcard() {}

    public Flashcard(String content, Integer timeTaken, LocalDate date, LocalTime time) {
        this.content = content;
        this.timeTaken = timeTaken;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public String getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(String flashcardId) {
        this.flashcardId = flashcardId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
