package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
    
    List<Reminder> findByUserUserId(Integer userId);
    
    List<Reminder> findByUserUserIdAndDate(Integer userId, LocalDate date);
    
    List<Reminder> findByUserUserIdOrderByDateAscTimeAsc(Integer userId);
    
    List<Reminder> findByProjectProjectId(Integer projectId);
}
