package com.edumate.backend.controller;

import com.edumate.backend.entity.mongodb.MCQ;
import com.edumate.backend.entity.sql.Note;
import com.edumate.backend.entity.sql.Project;
import com.edumate.backend.entity.sql.User;
import com.edumate.backend.repository.sql.NoteRepository;
import com.edumate.backend.repository.sql.ProjectRepository;
import com.edumate.backend.repository.sql.UserRepository;
import com.edumate.backend.service.ContentGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ContentGenerationService contentGenerationService;

    public NoteController(NoteRepository noteRepository, 
                         UserRepository userRepository,
                         ProjectRepository projectRepository,
                         ContentGenerationService contentGenerationService) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.contentGenerationService = contentGenerationService;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getNotes(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Note> notes = noteRepository.findByUser(user);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Note> getNote(@PathVariable String noteId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<Note> note = noteRepository.findByNoteIdAndUser(noteId, user);
        return note.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody NoteRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Project project = projectRepository.findById(request.projectId).orElseThrow();
        
        Note note = new Note();
        note.setNoteId(java.util.UUID.randomUUID().toString());
        note.setTitle(request.title);
        note.setSummary(request.summary);
        note.setUser(user);
        note.setProject(project);
        
        Note savedNote = noteRepository.save(note);
        return ResponseEntity.ok(savedNote);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Note> updateNote(@PathVariable String noteId, 
                                         @Valid @RequestBody NoteRequest request, 
                                         Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<Note> noteOpt = noteRepository.findByNoteIdAndUser(noteId, user);
        
        if (noteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Note note = noteOpt.get();
        note.setTitle(request.title);
        note.setSummary(request.summary);
        
        Note updatedNote = noteRepository.save(note);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable String noteId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<Note> note = noteRepository.findByNoteIdAndUser(noteId, user);
        
        if (note.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        noteRepository.delete(note.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Note> uploadFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("title") String title,
                                         @RequestParam("projectId") Integer projectId,
                                         Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            Project project = projectRepository.findById(projectId).orElseThrow();
            
            Note note = contentGenerationService.processUploadedFile(file, title, user, project);
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{noteId}/generate-mcqs")
    public ResponseEntity<List<MCQ>> generateMCQs(@PathVariable String noteId,
                                                @RequestParam(defaultValue = "5") int numberOfQuestions,
                                                Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            
            // Verify note belongs to user
            Optional<Note> noteOpt = noteRepository.findByNoteIdAndUser(noteId, user);
            if (noteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            List<MCQ> mcqs = contentGenerationService.generateMCQsForNote(noteId, numberOfQuestions);
            return ResponseEntity.ok(mcqs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{noteId}/generate-flashcards")
    public ResponseEntity<String> generateFlashcards(@PathVariable String noteId,
                                                   @RequestParam(defaultValue = "10") int numberOfCards,
                                                   Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            
            // Verify note belongs to user
            Optional<Note> noteOpt = noteRepository.findByNoteIdAndUser(noteId, user);
            if (noteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            String flashcards = contentGenerationService.generateFlashcardsForNote(noteId, numberOfCards);
            return ResponseEntity.ok(flashcards);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class NoteRequest {
        public String title;
        public String summary;
        public Integer projectId;
    }
}
