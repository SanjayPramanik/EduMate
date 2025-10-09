package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Note;
import com.edumate.backend.entity.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
    
    List<Note> findByUser(User user);
    
    Optional<Note> findByNoteIdAndUser(String noteId, User user);
    
    List<Note> findByUserUserId(Integer userId);
    
    List<Note> findByProjectProjectId(Integer projectId);
    
    List<Note> findByUserUserIdAndProjectProjectId(Integer userId, Integer projectId);
    
    List<Note> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
