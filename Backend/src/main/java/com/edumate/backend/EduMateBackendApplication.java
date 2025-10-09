package com.edumate.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.edumate.backend.repository.sql")
@EnableMongoRepositories(basePackages = "com.edumate.backend.repository.mongodb")
public class EduMateBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduMateBackendApplication.class, args);
    }
}
