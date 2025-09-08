package com.edumate.backend.repository.mongodb;

import com.edumate.backend.entity.mongodb.Flashcard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlashcardRepository extends MongoRepository<Flashcard, String> {
    
    List<Flashcard> findByDate(LocalDate date);
    
    List<Flashcard> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
