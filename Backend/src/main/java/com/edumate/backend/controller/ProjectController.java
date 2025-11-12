package com.edumate.backend.controller;

import com.edumate.backend.entity.sql.Project;
import com.edumate.backend.entity.sql.User;
import com.edumate.backend.repository.sql.ProjectRepository;
import com.edumate.backend.repository.sql.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Project> projects = projectRepository.findByUser(user);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Integer projectId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<Project> project = projectRepository.findByProjectIdAndUser(projectId, user);
        return project.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        
        Project project = new Project();
        project.setProjectName(request.name);
        project.setProjectSummary(request.description);
        project.setUser(user);
        
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer projectId, 
                                               @Valid @RequestBody ProjectRequest request, 
                                               Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<Project> projectOpt = projectRepository.findByProjectIdAndUser(projectId, user);
        
        if (projectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Project project = projectOpt.get();
        project.setProjectName(request.name);
        project.setProjectSummary(request.description);
        
        Project updatedProject = projectRepository.save(project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer projectId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<Project> project = projectRepository.findByProjectIdAndUser(projectId, user);
        
        if (project.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        projectRepository.delete(project.get());
        return ResponseEntity.ok().build();
    }

    public static class ProjectRequest {
        public String name;
        public String description;
    }
}
