package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
    
    List<Note> findByUserUserId(Integer userId);
    
    List<Note> findByProjectProjectId(Integer projectId);
    
    List<Note> findByUserUserIdAndProjectProjectId(Integer userId, Integer projectId);
    
    List<Note> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
