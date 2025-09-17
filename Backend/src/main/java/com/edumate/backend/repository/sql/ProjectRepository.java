package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    
    List<Project> findByUserUserId(Integer userId);
    
    List<Project> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
