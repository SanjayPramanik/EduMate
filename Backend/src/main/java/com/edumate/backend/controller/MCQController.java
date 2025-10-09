package com.edumate.backend.controller;

import com.edumate.backend.entity.mongodb.MCQ;
import com.edumate.backend.repository.mongodb.MCQRepository;
import com.edumate.backend.repository.sql.UserRepository;
import com.edumate.backend.entity.sql.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcqs")
public class MCQController {

    private final MCQRepository mcqRepository;
    private final UserRepository userRepository;

    public MCQController(MCQRepository mcqRepository, UserRepository userRepository) {
        this.mcqRepository = mcqRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<MCQ>> getMCQs(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<MCQ> mcqs = mcqRepository.findByUserId(user.getUserId().toString());
        return ResponseEntity.ok(mcqs);
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<List<MCQ>> getMCQsByNote(@PathVariable String noteId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<MCQ> mcqs = mcqRepository.findByNoteId(noteId);
        
        // Filter to ensure user owns the note
        List<MCQ> userMCQs = mcqs.stream()
                .filter(mcq -> mcq.getUserId().equals(user.getUserId().toString()))
                .toList();
        
        return ResponseEntity.ok(userMCQs);
    }

    @GetMapping("/{mcqId}")
    public ResponseEntity<MCQ> getMCQ(@PathVariable String mcqId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        
        return mcqRepository.findById(mcqId)
                .filter(mcq -> mcq.getUserId().equals(user.getUserId().toString()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{mcqId}")
    public ResponseEntity<Void> deleteMCQ(@PathVariable String mcqId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        
        return mcqRepository.findById(mcqId)
                .filter(mcq -> mcq.getUserId().equals(user.getUserId().toString()))
                .map(mcq -> {
                    mcqRepository.delete(mcq);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}


