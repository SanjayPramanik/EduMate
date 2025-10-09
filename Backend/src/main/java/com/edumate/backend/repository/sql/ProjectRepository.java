package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Project;
import com.edumate.backend.entity.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    
    List<Project> findByUser(User user);
    
    Optional<Project> findByProjectIdAndUser(Integer projectId, User user);
    
    List<Project> findByUserUserId(Integer userId);
    
    List<Project> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
