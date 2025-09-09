package com.edumate.backend.entity.sql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    @NotBlank(message = "Task text is required")
    @Column(name = "task_text", nullable = false)
    private String taskText;

    @Column(name = "task_deadline")
    private LocalDate taskDeadline;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    // Relationships
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reminder> reminders;

    // Constructors
    public Task() {}

    public Task(String taskText, LocalDate taskDeadline) {
        this.taskText = taskText;
        this.taskDeadline = taskDeadline;
        this.isCompleted = false;
    }

    // Getters and Setters
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(LocalDate taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }
}
