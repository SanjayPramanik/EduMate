package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    
    List<Task> findByIsCompleted(Boolean isCompleted);
    
    List<Task> findByTaskDeadline(LocalDate deadline);
    
    List<Task> findByTaskDeadlineBeforeAndIsCompleted(LocalDate deadline, Boolean isCompleted);
}
