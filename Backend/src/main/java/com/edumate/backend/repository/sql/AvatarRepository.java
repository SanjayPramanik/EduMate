package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
    
    List<Avatar> findByUserUserId(Integer userId);
}
