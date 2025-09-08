package com.edumate.backend.repository.mongodb;

import com.edumate.backend.entity.mongodb.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    
    List<Quiz> findByAttemptDate(LocalDate attemptDate);
    
    List<Quiz> findByIsCorrect(Boolean isCorrect);
    
    List<Quiz> findByAttemptDateBetween(LocalDate startDate, LocalDate endDate);
}
