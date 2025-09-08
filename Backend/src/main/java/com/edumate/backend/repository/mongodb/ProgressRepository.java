package com.edumate.backend.repository.mongodb;

import com.edumate.backend.entity.mongodb.Progress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgressRepository extends MongoRepository<Progress, String> {
    
    List<Progress> findByUserId(String userId);
    
    List<Progress> findByUserIdAndProjectId(String userId, String projectId);
    
    List<Progress> findByStudyDate(LocalDate studyDate);
    
    List<Progress> findByUserIdAndStudyDateBetween(String userId, LocalDate startDate, LocalDate endDate);
}
