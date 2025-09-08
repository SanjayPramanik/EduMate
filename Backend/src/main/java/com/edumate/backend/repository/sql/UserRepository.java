package com.edumate.backend.repository.sql;

import com.edumate.backend.entity.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.username = :username")
    Optional<User> findByEmailOrUsername(@Param("email") String email, @Param("username") String username);
}
