package com.edumate.backend.repository.mongodb;

import com.edumate.backend.entity.mongodb.MCQ;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MCQRepository extends MongoRepository<MCQ, String> {
    
    List<MCQ> findByUserId(String userId);
    
    List<MCQ> findByUserIdAndProjectId(String userId, String projectId);
    
    List<MCQ> findByNoteId(String noteId);
}
