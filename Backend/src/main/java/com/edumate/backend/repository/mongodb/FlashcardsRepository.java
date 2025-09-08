package com.edumate.backend.repository.mongodb;

import com.edumate.backend.entity.mongodb.Flashcards;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardsRepository extends MongoRepository<Flashcards, String> {
    
    List<Flashcards> findByUserId(String userId);
    
    List<Flashcards> findByUserIdAndProjectId(String userId, String projectId);
    
    List<Flashcards> findByNoteId(String noteId);
}
